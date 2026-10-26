package com.novatristudio.clipcut

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val ClipCutYellow = Color(0xFFFFD700)
val DarkBg = Color(0xFF0A0A0A)
val CardBg = Color(0xFF161616)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClipCutApp()
        }
    }
}

@Composable
fun ClipCutApp() {
    var currentScreen by remember { mutableStateOf("home") }
    var showPaywall by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(DarkBg)) {
        Column(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                "home" -> HomeScreen(
                    onNewProject = { currentScreen = "editor" },
                    onOpenPaywall = { showPaywall = true }
                )
                "editor" -> EditorScreen(
                    onBack = { currentScreen = "home" },
                    onExport = { showPaywall = true }
                )
            }
        }

        if (showPaywall) {
            PaywallDialog(onDismiss = { showPaywall = false })
        }
    }
}

@Composable
fun HomeScreen(onNewProject: () -> Unit, onOpenPaywall: () -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("ClipCut", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("BY NOVATRISTUDIO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ClipCutYellow)
            }
            Button(
                onClick = onOpenPaywall,
                colors = ButtonDefaults.buttonColors(containerColor = ClipCutYellow),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("★ PRO", color = Color.Black, fontWeight = FontWeight.Black)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().height(120.dp).clickable { onNewProject() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ClipCutYellow)
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("+ Nuevo Proyecto", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    Text("Multipista 4K / 60FPS", fontSize = 12.sp, color = Color.Black.copy(0.8f))
                }
                Icon(Icons.Default.AddCircle, null, tint = Color.Black, modifier = Modifier.size(36.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("CREACIÓN RÁPIDA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickBox("Editar Foto", Icons.Default.Image, Modifier.weight(1f)) {
                Toast.makeText(context, "Editor de Fotos ClipCut", Toast.LENGTH_SHORT).show()
            }
            QuickBox("Lab IA", Icons.Default.AutoAwesome, Modifier.weight(1f)) {
                Toast.makeText(context, "Laboratorio IA (Texto a Video)", Toast.LENGTH_SHORT).show()
            }
            QuickBox("Cámara Pro", Icons.Default.Videocam, Modifier.weight(1f)) {
                Toast.makeText(context, "Cámara 4K HDR", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun QuickBox(title: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(80.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterVertically,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = ClipCutYellow, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EditorScreen(onBack: () -> Unit, onExport: () -> Unit) {
    var isPlaying by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Default.Close, null, tint = Color.White) }
            Text("ClipCut Studio", color = Color.White, fontWeight = FontWeight.Bold)
            Button(
                onClick = onExport,
                colors = ButtonDefaults.buttonColors(containerColor = ClipCutYellow),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Exportar", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().weight(0.4f).background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("Previsualización 4K", color = Color.DarkGray)
            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.Black.copy(0.6f))
            ) {
                Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = ClipCutYellow)
            }
        }

        Column(modifier = Modifier.fillMaxWidth().weight(0.4f).background(Color(0xFF111111)).padding(8.dp)) {
            TrackBar("Video 4K", ClipCutYellow)
            Spacer(modifier = Modifier.height(4.dp))
            TrackBar("Audio / Voz", Color(0xFF00E5FF))
            Spacer(modifier = Modifier.height(4.dp))
            TrackBar("Subtítulos IA", Color(0xFFFF007A))
        }

        Row(
            modifier = Modifier.fillMaxWidth().height(65.dp).background(CardBg).horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            ToolBtn("Dividir", Icons.Default.ContentCut)
            ToolBtn("Velocidad", Icons.Default.Speed)
            ToolBtn("IA Fix", Icons.Default.AutoFixHigh)
            ToolBtn("Chroma", Icons.Default.ColorLens)
            ToolBtn("LUTs", Icons.Default.FilterVintage)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun TrackBar(label: String, color: Color) {
    Box(
        modifier = Modifier.fillMaxWidth().height(30.dp).clip(RoundedCornerShape(6.dp))
            .background(color.copy(0.2f)).border(1.dp, color, RoundedCornerShape(6.dp)).padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(label, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ToolBtn(title: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
        Text(title, color = Color.LightGray, fontSize = 10.sp)
    }
}

@Composable
fun PaywallDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.95f)).padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, null, tint = Color.White) }
            }
            Text("ClipCut PRO", fontSize = 26.sp, fontWeight = FontWeight.Black, color = ClipCutYellow)
            Text("Exporta en 4K 60FPS sin marca de agua", fontSize = 12.sp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(20.dp))

            PlanRow("Prueba VIP (7 Días)", "$20 MXN")
            PlanRow("Plan Mensual", "$100 MXN")
            PlanRow("Plan Anual (Mejor Opción)", "$1,000 MXN")

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    Toast.makeText(context, "Conectando con Google Play Billing...", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ClipCutYellow),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Continuar con Google Play", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PlanRow(title: String, price: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(price, color = ClipCutYellow, fontWeight = FontWeight.Black, fontSize = 13.sp)
        }
    }
}
