package com.example.myapplication.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.example.myapplication.domain.Assignment
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UpdateAssignmentActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val assignment =
                Json.decodeFromString<Assignment>(intent.getStringExtra("assignment")!!)
            var subject by remember { mutableStateOf(assignment.subject) }
            var description by remember { mutableStateOf(assignment.description) }
            var deadline by remember {
                mutableStateOf(
                    assignment.deadline.toString().split("T")[0]
                )
            }
            var receiveDate by remember {
                mutableStateOf(
                    assignment.received.toString().split("T")[0]
                )
            }
            var delivered by remember { mutableStateOf(assignment.isDelivered) }
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
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {
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
                                        Log.d("info", assignment.id.toString())
                                        val newAssignment = Assignment(
                                            assignment.id,
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
                                            Json.encodeToString(newAssignment)
                                        )
                                        setResult(Activity.RESULT_OK, resultIntent)
                                        finish()
                                    } catch (e: Exception) {
                                        error = e.message!!
                                    }
                                },
                                shape = ButtonDefaults.elevatedShape
                            ) {
                                Text(text = "Update assignment")
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

        if (!deadline.matches(dateRegex)) {
            throw Exception("deadline is not a valid date")
        }

        if (!receivedDate.matches(dateRegex)) {
            throw Exception("Received date is not a valid date")
        }
    }
}
