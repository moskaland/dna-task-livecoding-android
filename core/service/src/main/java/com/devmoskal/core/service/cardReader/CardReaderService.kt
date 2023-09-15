package com.devmoskal.core.service.cardReader

import com.devmoskal.core.model.CardData

interface CardReaderService {
    suspend fun readCard(): CardData
}