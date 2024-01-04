package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.activities.AddAssignmentActivity
import com.example.myapplication.activities.UpdateAssignmentActivity
import com.example.myapplication.domain.Assignment
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    val assignments = mutableStateListOf<Assignment>(
        Assignment(
            1,
            "description",
            Instant.fromEpochSeconds(1698527357),
            Instant.fromEpochSeconds(1698529000),
            "subject",
            false
        ),
        Assignment(
            2,
            "description",
            Instant.fromEpochSeconds(1698527357),
            Instant.fromEpochSeconds(1698529000),
            "subject",
            true
        ),
        Assignment(
            3,
            "description",
            Instant.fromEpochSeconds(1698527357),
            Instant.fromEpochSeconds(1698529000),
            "subject",
            false
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        addEntityButton()
                    }
                }
            }
        }
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                var assignment =
                    Json.decodeFromString<Assignment>(data!!.getStringExtra("assignment")!!)
                Log.d("assignment", assignment.toString())
                assignments.add(assignment)
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                var assignment =
                    Json.decodeFromString<Assignment>(data!!.getStringExtra("assignment")!!)
                Log.d("assignment", assignment.toString())
                var res = assignments.removeIf {
                    assignment.id == it.id
                }
                Log.d("info", res.toString())
                assignments.add(assignment)
            }
        }
    }

    @Composable
    private fun addEntityButton() {
        val addIntent = Intent(this@MainActivity, AddAssignmentActivity::class.java)
        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = {
                    startActivityForResult(addIntent, 1)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_circle_24),
                    contentDescription = "Add assignment",
                    tint = Color.Blue
                )
            }
            AssignmentList()
        }
    }


    @Composable
    private fun AssignmentList() {
        LazyColumn(
            //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                items = assignments,
                itemContent = {
                    AssignmentListItem(assignment = it)
                }
            )
        }
    }

    @Composable
    private fun AssignmentListItem(assignment: Assignment) {
        val updateIntent = Intent(this@MainActivity, UpdateAssignmentActivity::class.java)
        val openAlertDialog = remember { mutableStateOf(false) }

        if (openAlertDialog.value) {
            AreYouSureModal({
                assignments.remove(assignment)
                openAlertDialog.value = false
            }, {
                openAlertDialog.value = false
            })
        }

        return Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE4D88E))
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(
                    text = assignment.description,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
                Text(
                    text = assignment.subject,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
                Text(
                    text = assignment.deadline.toString().split("T")[0],
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
                Text(
                    text = assignment.received.toString().split("T")[0],
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
                Text(
                    text = if (assignment.isDelivered) "Delivered" else "Not delivered",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(Modifier.clickable(onClick = {
                    openAlertDialog.value = true
                })) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
                IconButton(onClick = {
                    updateIntent.putExtra("assignment", Json.encodeToString(assignment))
                    startActivityForResult(updateIntent, 2)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                        contentDescription = "Edit",
                        tint = Color.Yellow
                    )
                }
            }
        }

    }

    @Composable
    fun AreYouSureModal(
        onConfirmation: () -> Unit,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            title = {
                Text(text = "Are you sure")
            },
            text = {
                Text(text = "Are you sure you want to delete this?")
            },
            onDismissRequest = {

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Close")
                }
            }
        )
    }
}
