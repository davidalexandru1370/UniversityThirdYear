package com.example.myapplication.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.activities.ui.theme.MyApplicationTheme
import com.example.myapplication.domain.Assignment
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.format.DateTimeFormatter
import java.util.Random

class AddAssignmentActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var subject by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var deadline by remember { mutableStateOf("") }
            var receiveDate by remember { mutableStateOf("") }
            var delivered by remember { mutableStateOf(false) }
            var error by remember { mutableStateOf("") }

            com.example.myapplication.ui.theme.MyApplicationTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        TextField(
                            value = description,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = { description = it },
                            label = { Text("Description") })
                        TextField(
                            value = subject,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = { subject = it },
                            label = { Text("Subject") })
                        TextField(
                            value = receiveDate,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = { receiveDate = it },
                            label = { Text("Receive date") })
                        TextField(
                            value = deadline,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = { deadline = it },
                            label = { Text("Deadline") })
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Delivered")
                            Checkbox(checked = delivered, onCheckedChange = {
                                delivered = it
                            })
                        }
                        Button(
                            onClick = {
                                try {
                                    addAssignmentHandler(
                                        subject,
                                        description,
                                        deadline,
                                        receiveDate
                                    )
                                    val resultIntent: Intent = Intent()

                                    val strTime = "00:00"
                                    val assignment = Assignment(
                                        kotlin.random.Random.nextInt(0, 1000000),
                                        description,
                                        LocalDateTime.parse(deadline + "T" + strTime)
                                            .toInstant(TimeZone.UTC),
                                        LocalDateTime.parse(receiveDate + "T" + strTime)
                                            .toInstant(TimeZone.UTC),
                                        subject,
                                        delivered
                                    )
                                    resultIntent.putExtra(
                                        "assignment",
                                        Json.encodeToString(assignment)
                                    )
                                    setResult(Activity.RESULT_OK, resultIntent)
                                    finish()
                                } catch (e: Exception) {
                                    error = e.message!!
                                }
                            },
                            shape = ButtonDefaults.elevatedShape
                        ) {
                            Text(text = "Add assignment")
                        }

                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = error,
                                style = TextStyle(color = androidx.compose.ui.graphics.Color.Red)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun addAssignmentHandler(
        subject: String,
        description: String,
        deadline: String,
        receivedDate: String
    ) {
        if (subject.length < 5) {
            throw Exception("Subject length must be greater than 5")
        }

        if (description.length < 5) {
            throw Exception("Description length must be greater than 5")
        }

        var dateRegex = Regex("2023-[0-9][0-9]-[0-9][0-9]")
//
//        if (!deadline.matches(dateRegex)) {
//            throw Exception("deadline is not a valid date")
//        }
//
//        if (!receivedDate.matches(dateRegex)) {
//            throw Exception("Received date is not a valid date")
//        }
    }
}
