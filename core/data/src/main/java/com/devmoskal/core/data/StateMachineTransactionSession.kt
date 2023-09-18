package com.devmoskal.core.data

import com.devmoskal.core.common.Result
import com.devmoskal.core.common.StateMachine
import com.devmoskal.core.common.di.DefaultDispatcher
import com.devmoskal.core.data.SessionState.CanceledUnpaid
import com.devmoskal.core.data.SessionState.ConfirmedPaid
import com.devmoskal.core.data.SessionState.FailedPaid
import com.devmoskal.core.data.SessionState.FailedUnpaid
import com.devmoskal.core.data.SessionState.UnconfirmedPaid
import com.devmoskal.core.data.SessionState.UnconfirmedUnpaid
import com.devmoskal.core.data.SessionState.Uninitialized
import com.devmoskal.core.data.model.InvalidTransitionException
import com.devmoskal.core.data.model.TransactionEvent
import com.devmoskal.core.data.model.TransactionEvent.Cancel
import com.devmoskal.core.data.model.TransactionEvent.Confirm
import com.devmoskal.core.data.model.TransactionEvent.Initialize
import com.devmoskal.core.data.model.TransactionEvent.Pay
import com.devmoskal.core.data.model.TransactionEvent.Refund
import com.devmoskal.core.data.model.TransactionEvent.TransactionFail
import com.devmoskal.core.datasource.TransactionSessionDataSource
import com.devmoskal.core.model.PaymentInfo
import com.devmoskal.core.model.TransactionSessionData
import com.devmoskal.core.model.TransactionStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Work In Progress.
 *
 * I've used state machine to limit flow to only legal transition.
 * Yet, I've done it quite late during implementation, so I do not utilize its benefit.
 * As for now state machine it's mostly unused, rather as interesting concept than real part of system.
 */
internal class StateMachineTransactionSession @Inject constructor(
    private val dataSource: TransactionSessionDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : TransactionSession {
    override val state: StateFlow<TransactionSessionData?> = dataSource.data
    private val stateMachine = defineStateMachine()

    override suspend fun process(event: TransactionEvent): Result<Unit, InvalidTransitionException> =
        withContext(defaultDispatcher) {
            when (stateMachine.transition(event)) {
                is StateMachine.Transition.Valid -> {
                    updateSessionData(event)
                }

                is StateMachine.Transition.Invalid -> Result.Failure(InvalidTransitionException)
            }
        }

    override suspend fun clear() {
        dataSource.clear()
    }

    private suspend fun updateSessionData(event: TransactionEvent): Result<Unit, InvalidTransitionException> {
        dataSource.mutex.withLock {
            val currentData = dataSource.data.value
            val newData: TransactionSessionData = when (event) {
                is Initialize -> TransactionSessionData(
                    event.transaction.transactionID,
                    event.transaction.status,
                    event.transaction.order,
                    event.totalValue
                )

                Cancel -> currentData?.copy(status = TransactionStatus.CANCELLED)
                Confirm -> currentData?.copy(status = TransactionStatus.CONFIRMED)
                is Pay -> currentData?.copy(paymentInfo = PaymentInfo.Paid(event.cardToken))
                Refund -> currentData?.copy(paymentInfo = PaymentInfo.Refunded)
                TransactionFail -> currentData?.copy(status = TransactionStatus.FAILED)
            } ?: return Result.Failure(InvalidTransitionException)
            dataSource.setData(newData)
        }
        return Result.Success
    }

    private fun defineStateMachine() = StateMachine.create<SessionState, TransactionEvent, Unit> {
        initialState(Uninitialized)
        state<Uninitialized> {
            on<Initialize> {
                transitionTo(UnconfirmedUnpaid)
            }
        }

        state<UnconfirmedUnpaid> {
            on<Pay> { transitionTo(UnconfirmedPaid) }
            on<TransactionFail> { transitionTo(FailedUnpaid) }
            on<Cancel> { transitionTo(CanceledUnpaid) }
        }

        state<UnconfirmedPaid> {
            on<Confirm> { transitionTo(ConfirmedPaid) }
            on<Refund> { transitionTo(UnconfirmedUnpaid) }
            on<TransactionFail> { transitionTo(FailedPaid) }
        }

        state<FailedPaid> {
            on<Refund> { transitionTo(ConfirmedPaid) }
        }

        state<ConfirmedPaid> {}
        state<FailedUnpaid> {}
        state<CanceledUnpaid> {}
    }
}

internal sealed class SessionState {
    object Uninitialized : SessionState()
    object UnconfirmedUnpaid : SessionState()
    object UnconfirmedPaid : SessionState()
    object FailedPaid : SessionState()
    object ConfirmedPaid : SessionState()
    object FailedUnpaid : SessionState()
    object CanceledUnpaid : SessionState()
}