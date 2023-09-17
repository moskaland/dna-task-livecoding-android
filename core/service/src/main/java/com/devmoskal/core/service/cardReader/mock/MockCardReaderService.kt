package com.devmoskal.core.service.cardReader.mock

import com.devmoskal.core.model.CardData
import com.devmoskal.core.service.cardReader.CardReaderService
import com.devmoskal.core.service.cardReader.model.CardReaderException
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

internal class MockCardReaderService @Inject constructor() : CardReaderService {

    override suspend fun readCard(): CardData {
        val second = Calendar.getInstance().get(Calendar.SECOND)

        if (second <= 5) {
            // User will need some time to use the card
            delay(4000)
            return CardData(UUID.randomUUID().toString())
        }

        throw CardReaderException()
    }

}