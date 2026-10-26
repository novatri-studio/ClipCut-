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
val ClipCutYellowLight = Color(0xFFFFF066)
val DarkBg = Color(0xFF0F0F0F)
val CardBg = Color(0xFF1C1C1E)

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
    Column(modifier = Modifier.fillMaxSize()) {
        // ENCABEZADO AMARILLO VIBRANTE
        Surface(
            color = ClipCutYellow,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ClipCut", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    Text("BY NOVATRISTUDIO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black.copy(0.7f))
                }
                Button(
                    onClick = onOpenPaywall,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("★ PRO", color = ClipCutYellow, fontWeight = FontWeight.Black)
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // TARJETA PRINCIPAL AMARILLA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clickable { onNewProject() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ClipCutYellow)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("+ Nuevo Proyecto", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.Black)
                        Text("Línea Multipista 4K / 60FPS", fontSize = 13.sp, color = Color.Black.copy(0.8f))
                    }
                    Icon(Icons.Default.AddCircle, null, tint = Color.Black, modifier = Modifier.size(44.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("HERRAMIENTAS CLIPCUT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ClipCutYellow)
            Spacer(modifier = Modifier.height(10.dp))

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

            Spacer(modifier = Modifier.height(24.dp))
            Text("PROYECTOS RECIENTES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ClipCutYellow)
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBg)
                    .border(1.dp, ClipCutYellow.copy(0.3f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Toca en '+ Nuevo Proyecto' para editar tu primer video.", color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun QuickBox(title: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .height(90.dp)
            .clickable { onClick() }
            .border(1.dp, ClipCutYellow.copy(0.4f), RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = ClipCutYellow, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EditorScreen(onBack: () -> Unit, onExport: () -> Unit) {
    var isPlaying by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ClipCutYellow)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Default.Close, null, tint = Color.Black) }
            Text("ClipCut Studio", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 16.sp)
            Button(
                onClick = onExport,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Exportar", color = ClipCutYellow, fontWeight = FontWeight.Bold)
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().weight(0.4f).background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("Previsualización 4K UHD", color = Color.DarkGray)
            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier.size(54.dp).clip(CircleShape).background(Color.Black.copy(0.7f)).border(2.dp, ClipCutYellow, CircleShape)
            ) {
                Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = ClipCutYellow, modifier = Modifier.size(32.dp))
            }
        }

        Column(modifier = Modifier.fillMaxWidth().weight(0.4f).background(Color(0xFF141414)).padding(10.dp)) {
            TrackBar("Video 4K", ClipCutYellow)
            Spacer(modifier = Modifier.height(6.dp))
            TrackBar("Audio / Voz", Color(0xFF00E5FF))
            Spacer(modifier = Modifier.height(6.dp))
            TrackBar("Subtítulos IA", Color(0xFFFF007A))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(CardBg)
                .border(1.dp, ClipCutYellow.copy(0.2f))
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            ToolBtn("Dividir", Icons.Default.ContentCut)
            ToolBtn("Velocidad", Icons.Default.Speed)
            ToolBtn("IA Fix", Icons.Default.AutoFixHigh)
            ToolBtn("Chroma", Icons.Default.ColorLens)
            ToolBtn("LUTs", Icons.Default.FilterVintage)
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun TrackBar(label: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(0.25f))
            .border(1.5.dp, color, RoundedCornerShape(8.
