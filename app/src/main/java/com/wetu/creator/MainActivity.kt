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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    var status by remember { mutableStateOf("Prêt à créer") }
    var imageSelected by remember { mutableStateOf(false) }
    var tshilubaReady by remember { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        imageSelected = uri != null
        if (uri != null) {
            status = "Image importée avec succès"
            tshilubaReady = false
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("WETU", fontWeight = FontWeight.Bold)
                            Text("Creator Studio", style = MaterialTheme.typography.labelSmall)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 18.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(Modifier.height(4.dp))

                Text(
                    "Donnez vie à votre histoire.",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Importez une image, écrivez votre scénario en français et préparez sa version Tshiluba avant la génération vidéo.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Production intelligente",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Chaîne WETU : image → scénario → Tshiluba → Creator Core → vidéo.")
                    }
                }

                Text("1  •  Image source", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

                OutlinedButton(
                    onClick = { picker.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (imageSelected) "Image sélectionnée" else "Importer depuis le téléphone")
                }

                Text(
                    if (imageSelected) "✓ Source prête pour la production"
                    else "JPG, PNG ou image compatible du gestionnaire de fichiers",
                    style = MaterialTheme.typography.bodySmall
                )

                Text("2  •  Scénario", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

                OutlinedTextField(
                    value = scenario,
                    onValueChange = {
                        scenario = it
                        tshilubaReady = false
                        if (it.isNotBlank()) status = "Scénario en cours d'édition"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Votre scénario en français") },
                    placeholder = { Text("Ex. Une femme marche dans Kinshasa au coucher du soleil…") },
                    minLines = 6,
                    supportingText = { Text("${scenario.length} caractères") }
                )

                Text("3  •  Version Tshiluba", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (tshilubaReady) Icons.Default.CheckCircle else Icons.Default.AutoAwesome,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                if (tshilubaReady) "Tshiluba préparé" else "Traduction Tshiluba",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            if (tshilubaReady)
                                "La version Tshiluba est prête pour l'étape Creator Core."
                            else
                                "Préparez la version Tshiluba à partir du scénario français."
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {
                                tshilubaReady = true
                                status = "Version Tshiluba préparée"
                            },
                            enabled = scenario.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Préparer le Tshiluba")
                        }
                    }
                }

                Button(
                    onClick = { status = "Création vidéo demandée • Creator Core" },
                    enabled = imageSelected && scenario.isNotBlank() && tshilubaReady,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Movie, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Lancer la création vidéo")
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("État : $status", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                            Text("Les clés secrètes restent hors de l'application.", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Sécurité intégrée", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text(
                                "HTTPS uniquement • Creator Core séparé • aucune clé secrète embarquée",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))
            }
        }
    }
}
