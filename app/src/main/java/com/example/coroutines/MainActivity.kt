package com.example.coroutines

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        compareUsingAsyncAwait()

        coroutineChannel()

        coroutineFlow()

    }
    /**
     * This method uses to compare two values from different sources they take different time to be ready
     * Suspend comparison until the data be ready using await method
     */
    private fun compareUsingAsyncAwait() {
        GlobalScope.launch {

            val time = measureTimeMillis {
                val userDB = async { userFromDB() }
                val userServer = async { userFromServer() }

                if (userDB.await() == userServer.await()) {
                    Log.d("Coroutine", "Equals")
                } else {
                    Log.d("Coroutine", " not Equals")
                }
            }
            Log.d("Coroutine", time.toString())

        }
    }

    /**
     * This method simulates bringing the data from locally database like sqlite
     */
    suspend fun userFromDB(): String {
        delay(2000)
        Log.d("Coroutine", "raneem1")
        return "raneem"
    }

    /**
     * This method simulates bringing the data from server such as firebase
     */
    suspend fun userFromServer(): String {
        delay(3000)
        Log.d("Coroutine", "raneem2")
        return "raneem"
    }


    /**
     * This method simulates sending and accepting a stream of data
     */
    private fun coroutineChannel() {

        val kotlinChannel = Channel<String>(3)
        val list = arrayOf("A", "B", "C", "D")

        runBlocking {
            launch {
                for (char in list) {
                    kotlinChannel.send(char)
                    delay(1000)
                }
            }

            launch {
                for (char in kotlinChannel) {
                    Log.d("Channel", char)
                }
            }

        }

    }

    /**
     * This method simulates sending and accepting a stream of data with making some of changes in data before send it to collector
     * Collector will observe the data produced from operators
     */
    private fun coroutineFlow() {

        runBlocking {

            //Producer

            flow<Int> {

                for (i in 1..10) {
                    emit(i)
                    Log.d("Flow",i.toString())
                }
            }.filter { i :Int -> i < 5 } // Intermediate
                //Collector
                .collect {
                    Log.d("Flow",it.toString())
                }
        }
    }
}