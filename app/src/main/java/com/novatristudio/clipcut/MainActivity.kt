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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

val ClipCutYellow = Color(0xFFEBB700)
val ClipCutYellowCard = Color(0xFFFFD53D)
val DarkPaywallBg = Color(0xFF141414)
val DarkCardBg = Color(0xFF1E1E1E)

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

    Box(modifier = Modifier.fillMaxSize().background(ClipCutYellow)) {
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
    var selectedTab by remember { mutableStateOf(0) }

    val tools = listOf(
        Pair("AutoCut", Icons.Default.PlayCircle),
        Pair("Retoque", Icons.Default.Face),
        Pair("Generador IA", Icons.Default.AutoAwesome),
        Pair("Herramientas foto", Icons.Default.Image),
        Pair("Disparar y grabar", Icons.Default.Videocam),
        Pair("Mejorar auto", Icons.Default.AutoFixHigh),
        Pair("Creador portadas", Icons.Default.Edit),
        Pair("Subtítulos auto", Icons.Default.ClosedCaption),
        Pair("Recortadora IA", Icons.Default.ContentCut),
        Pair("Eliminar fondo", Icons.Default.Person),
        Pair("Editor fotos IA", Icons.Default.PhotoFilter),
        Pair("Espacio", Icons.Default.Cloud),
        Pair("Ajustar velocidad", Icons.Default.Speed),
        Pair("Herramientas mkt", Icons.Default.ShoppingBag),
        Pair("Herramientas audio", Icons.Default.VolumeUp),
        Pair("Avatares", Icons.Default.AccountCircle),
        Pair("Multimedia", Icons.Default.Folder),
        Pair("Todas (34)", Icons.Default.Apps)
    )

    Column(modifier = Modifier.fillMaxSize().background(ClipCutYellow)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color.White.copy(alpha = 0.85f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.clickable { onOpenPaywall() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💎 Prueba Standard 7 días por $0", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text("Video create", fontSize = 14.sp, color = Color.Black.copy(0.7f), fontWeight = FontWeight.Medium)
            Text("Get started ›", fontSize = 26.sp, color = Color.Black, fontWeight = FontWeight.Black)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp)
                    .clickable { onNewProject() },
                colors = CardDefaults.cardColors(containerColor = ClipCutYellowCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.Black, modifier = Modifier.size(28.dp))
                    Text("Video nuevo", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp)
                    .clickable { Toast.makeText(context, "Editor de fotos", Toast.LENGTH_SHORT).show() },
                colors = CardDefaults.cardColors(containerColor = ClipCutYellowCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Image, null, tint = Color.Black, modifier = Modifier.size(28.dp))
                    Text("Editar foto", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(tools) { (title, icon) ->
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { Toast.makeText(context, title, Toast.LENGTH_SHORT).show() },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(icon, null, tint = Color.Black, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        title,
                        fontSize = 11.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }
        }

        NavigationBar(
            containerColor = ClipCutYellow,
            tonalElevation = 0.dp
        ) {
            val navItems = listOf(
                "Ediciones" to Icons.Default.ContentCut,
                "Plantillas" to Icons.Default.Layers,
                "Lab. de IA" to Icons.Default.AutoAwesome,
                "Biblioteca" to Icons.Default.Folder,
                "Yo" to Icons.Default.Person
            )
            navItems.forEachIndexed { index, (label, icon) ->
                NavigationBarItem(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    icon = { Icon(icon, null, tint = Color.Black) },
                    label = { Text(label, color = Color.Black, fontSize = 10.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = ClipCutYellowCard)
                )
            }
        }
    }
}

@Composable
fun EditorScreen(onBack: () -> Unit, onExport: () -> Unit) {
    var isPlaying by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
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
            modifier = Modifier.fillMaxWidth().weight(0.5f).background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("Previsualización 4K UHD", color = Color.DarkGray)
            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.7f))
                    .border(2.dp, ClipCutYellow, CircleShape)
            ) {
                Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = ClipCutYellow, modifier = Modifier.size(32.dp))
            }
        }

        Column(modifier = Modifier.fillMaxWidth().weight(0.35f).background(Color(0xFF141414)).padding(10.dp)) {
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
                .background(Color(0xFF1C1C1E))
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
            .border(1.5.dp, color, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ToolBtn(title: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Icon(icon, null, tint = ClipCutYellow, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, color = Color.White, fontSize = 11.sp)
    }
}

@Composable
fun PaywallDialog(onDismiss: () -> Unit) {
    var selectedPlan by remember { mutableStateOf("anual") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkPaywallBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, null, tint = Color.White)
                    }
                }

                Surface(
                    color = ClipCutYellow,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        " CLIPCUT PRO ",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Desbloquea Todo el Potencial de Edición",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Sin marcas de agua, exportación en 4K Ultra HD a 60 FPS, subtítulos ilimitados con IA y biblioteca completa.",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                PlanCard(
                    title = "Prueba de 7 Días",
                    price = "$20 MXN",
                    desc = "por 7 días",
                    isSelected = selectedPlan == "prueba",
                    onClick = { selectedPlan = "prueba" }
                )
                Spacer(modifier = Modifier.height(10.dp))
                PlanCard(
                    title = "Plan Mensual Pro",
                    price = "$100 MXN",
                    desc = "al mes",
                    isSelected = selectedPlan == "mensual",
                    onClick = { selectedPlan = "mensual" }
                )
                Spacer(modifier = Modifier.height(10.dp))
                PlanCard(
                    title = "Plan Anual Premium",
                    price = "$1,000 MXN",
                    desc = "al año (Ahorra 17%)",
                    badge = "MÁS POPULAR",
                    isSelected = selectedPlan == "anual",
                    onClick = { selectedPlan = "anual" }
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = ClipCutYellow),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Icon(Icons.Default.Bolt, null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Suscribirse con Google Play", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun PlanCard(title: String, price: String, desc: String, badge: String? = null, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) ClipCutYellow else Color.DarkGray,
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (badge != null) {
                Surface(color = ClipCutYellow, shape = RoundedCornerShape(4.dp)) {
                    Text(" $badge ", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(price, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                RadioButton(
                    selected = isSelected,
                    onClick = onClick,
                    colors = RadioButtonDefaults.colors(selectedColor = ClipCutYellow, unselectedColor = Color.Gray)
                )
            }
            Text(desc, color = Color.Gray, fontSize = 11.sp)
        }
    }
}
