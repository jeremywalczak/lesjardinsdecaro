package com.jekro.lesjardindecaro.repository

import com.jekro.lesjardindecaro.model.*
import com.jekro.lesjardindecaro.service.ConfigurationService
import io.paperdb.Paper
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit

class ConfigurationRepository (val service: ConfigurationService) {

    fun getConfiguration(): Single<Configuration> {
        return service.getConfiguration()
    }

    fun saveHistoricOrder(historicOrder: HistoricOrder) {
        insertInBook("historicOrder", historicOrder)
    }

    fun getHistoricOrder(): HistoricOrder? {
        return readFromBook("historicOrder")
    }

    fun saveCart(cart: Cart?) {
        insertInBook("cart", cart)
    }

    fun getCart(): Cart? {
        return readFromBook("cart")
    }

    fun saveUser(user: User) {
        insertInBook("user", user)
    }

    fun getUser(): User? {
        return readFromBook("user")
    }

    private fun <T> readFromBook(key: String): T? {
        try {
            return Paper.book().read<T>(key)
        } catch (e: Exception) {
        }
        return null
    }

    private fun <T> readFromBook(key: String, maxAge: Long?, unit: TimeUnit?): T? {
        val book = Paper.book()
        if (maxAge != null && unit != null && book.contains("$key-date")) {
            try {
                val read: Date? = book.read<Date>("$key-date")
                if (read != null && read.after(Date(Date().time - unit.toMillis(maxAge)))) {
                    return book.read<T>(key)
                }
            } catch (e: Exception) {
            }
        }
        return null
    }

    private fun insertInBook(key: String, value: Any?) {
        val book = Paper.book()
        if(value != null) {
            book.write("$key-date", Date())
            book.write(key, value)
        } else {
            book.delete("$key-date")
            book.delete(key)
        }
    }

}