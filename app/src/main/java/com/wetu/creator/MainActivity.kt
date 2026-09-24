package com.wetu.creator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { WetuApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@androidx.compose.runtime.Composable
private fun WetuApp() {
    var scenario by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Prêt") }
    var imageSelected by remember { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        imageSelected = uri != null
        if (uri != null) status = "Photo sélectionnée"
    }

    MaterialTheme {
        Scaffold(
            topBar = { SmallTopAppBar(title = { Text("WETU") }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Creator Studio", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "Transforme une photo et un scénario en production vidéo, avec une chaîne française → Tshiluba prête pour Creator Core."
                )

                OutlinedButton(
                    onClick = { picker.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                    Spacer(Modifier.padding(4.dp))
                    Text(if (imageSelected) "Photo sélectionnée" else "Choisir une photo")
                }

                OutlinedTextField(
                    value = scenario,
                    onValueChange = { scenario = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Scénario en français") },
                    minLines = 5
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Tshiluba", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            if (scenario.isBlank())
                                "La traduction sera préparée après saisie du scénario."
                            else
                                "Pipeline de traduction Tshiluba prêt à recevoir le scénario."
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { status = "Préparation Tshiluba" },
                        modifier = Modifier.weight(1f)
                    ) { Text("Préparer") }

                    Button(
                        onClick = { status = "Création vidéo demandée" },
                        modifier = Modifier.weight(1f),
                        enabled = imageSelected && scenario.isNotBlank()
                    ) {
                        Icon(Icons.Default.Movie, contentDescription = null)
                        Spacer(Modifier.padding(3.dp))
                        Text("Créer")
                    }
                }

                Text("État : $status", style = MaterialTheme.typography.labelLarge)
                Text(
                    "Sécurité : aucune clé secrète embarquée • HTTPS uniquement • Creator Core séparé",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
