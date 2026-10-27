package com.novatristudio.clipcut

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

val YellowGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFC79500), Color(0xFFE5B000), Color(0xFFF7C815))
)
val ClipCutYellowCard = Color(0xFFFFD53D)
val DarkPaywallBg = Color(0xFF141414)
val DarkCardBg = Color(0xFF1E1E1E)

data class NavItem(val label: String, val icon: ImageVector)
data class ToolItem(val title: String, val icon: ImageVector)

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
    var isLoggedIn by remember { mutableStateOf(false) }
    var loggedUser by remember { mutableStateOf("Usuario ClipCut") }
    var isProUser by remember { mutableStateOf(false) }

    var currentScreen by remember { mutableStateOf("home") }
    var currentTab by remember { mutableStateOf(0) }
    var showPaywall by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }

    val navItems = listOf(
        NavItem("Ediciones", Icons.Default.ContentCut),
        NavItem("Plantillas", Icons.Default.Layers),
        NavItem("Lab. de IA", Icons.Default.AutoAwesome),
        NavItem("Biblioteca", Icons.Default.Folder),
        NavItem("Yo", Icons.Default.Person)
    )

    if (!isLoggedIn) {
        LoginScreen(
            onLoginSuccess = { user ->
                loggedUser = user
                isLoggedIn = true
            }
        )
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(YellowGradient)) {
        if (currentScreen == "editor") {
            EditorScreen(
                mediaUri = selectedMediaUri,
                isPro = isProUser,
                onBack = { currentScreen = "home" },
                onExport = {
                    if (!isProUser) showPaywall = true
                }
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    when (currentTab) {
                        0 -> HomeScreen(
                            isPro = isProUser,
                            onMediaSelected = { uri ->
                                selectedMediaUri = uri
                                currentScreen = "editor"
                            },
                            onOpenPaywall = { showPaywall = true }
                        )
                        1 -> TemplatesScreen(
                            onTemplateSelected = {
                                selectedMediaUri = null
                                currentScreen = "editor"
                            }
                        )
                        2 -> AiLabScreen(
                            isPro = isProUser,
                            onOpenPaywall = { showPaywall = true }
                        )
                        3 -> LibraryScreen(
                            onMediaSelected = { uri ->
                                selectedMediaUri = uri
                                currentScreen = "editor"
                            }
                        )
                        4 -> ProfileScreen(
                            userName = loggedUser,
                            isPro = isProUser,
                            onOpenPaywall = { showPaywall = true },
                            onOpenSettings = { showSettings = true }
                        )
                    }
                }

                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = currentTab == index,
                            onClick = { currentTab = index },
                            icon = { Icon(item.icon, contentDescription = null, tint = Color.Black) },
                            label = {
                                Text(
                                    item.label,
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = if (currentTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = ClipCutYellowCard)
                        )
                    }
                }
            }
        }

        if (showPaywall) {
            PaywallDialog(
                onDismiss = { showPaywall = false },
                onSubscribeSuccess = {
                    isProUser = true
                    showPaywall = false
                }
            )
        }

        if (showSettings) {
            SettingsDialog(
                isPro = isProUser,
                onDismiss = { showSettings = false },
                onLogout = {
                    isLoggedIn = false
                    showSettings = false
                }
            )
        }
    }
}

// ----------------- PANTALLA 1: LOGIN -----------------
@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    val context = LocalContext.current
    var showImeiDialog by remember { mutableStateOf(false) }
    var imeiInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(YellowGradient)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    "Ayuda",
                    color = Color.Black.copy(0.7f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Soporte ClipCut", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("C", fontSize = 110.sp, fontWeight = FontWeight.Black, color = Color.Black)
                Text("ClipCut", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color.Black)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SocialLoginBtn("Iniciar sesión con TikTok", Color.Black, Color.White, Icons.Default.PlayCircle) {
                    onLoginSuccess("TikTok_User85")
                }
                SocialLoginBtn("Regístrate con Google", Color.White, Color.Black, Icons.Default.Email) {
                    onLoginSuccess("Google_User@gmail.com")
                }
                SocialLoginBtn("Iniciar sesión con Correo / IMEI", Color.White.copy(0.85f), Color.Black, Icons.Default.PhoneAndroid) {
                    showImeiDialog = true
                }
                SocialLoginBtn("Iniciar sesión con Facebook", Color.White, Color(0xFF1877F2), Icons.Default.AccountCircle) {
                    onLoginSuccess("Facebook_User")
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Leí y acepté los Términos del Servicio y la Política de Privacidad de ClipCut",
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp,
                    color = Color.Black.copy(0.75f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (showImeiDialog) {
            AlertDialog(
                onDismissRequest = { showImeiDialog = false },
                title = { Text("Ingresar con IMEI o Correo", fontWeight = FontWeight.Bold) },
                text = {
                    OutlinedTextField(
                        value = imeiInput,
                        onValueChange = { imeiInput = it },
                        placeholder = { Text("Número IMEI o correo...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        onClick = {
                            if (imeiInput.isNotBlank()) {
                                onLoginSuccess("ID_$imeiInput")
                                showImeiDialog = false
                            } else {
                                Toast.makeText(context, "Ingresa un dato válido", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Text("Acceder", color = ClipCutYellowCard)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showImeiDialog = false }) { Text("Cancelar", color = Color.Black) }
                }
            )
        }
    }
}

@Composable
fun SocialLoginBtn(text: String, bgColor: Color, textColor: Color, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick() },
        color = bgColor,
        shape = RoundedCornerShape(25.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text, color = textColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

// ----------------- PANTALLA 2: EDICIONES -----------------
@Composable
fun HomeScreen(isPro: Boolean, onMediaSelected: (Uri?) -> Unit, onOpenPaywall: () -> Unit) {
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onMediaSelected(uri)
        }
    }

    val tools = listOf(
        ToolItem("AutoCut", Icons.Default.PlayCircle),
        ToolItem("Retoque", Icons.Default.Face),
        ToolItem("Generador IA", Icons.Default.AutoAwesome),
        ToolItem("Herramientas foto", Icons.Default.Image),
        ToolItem("Disparar y grabar", Icons.Default.Videocam),
        ToolItem("Mejorar auto", Icons.Default.AutoFixHigh),
        ToolItem("Creador portadas", Icons.Default.Edit),
        ToolItem("Subtítulos auto", Icons.Default.ClosedCaption),
        ToolItem("Recortadora IA", Icons.Default.ContentCut),
        ToolItem("Eliminar fondo", Icons.Default.Person),
        ToolItem("Editor fotos IA", Icons.Default.PhotoFilter),
        ToolItem("Espacio", Icons.Default.Cloud),
        ToolItem("Ajustar velocidad", Icons.Default.Speed),
        ToolItem("Herramientas mkt", Icons.Default.ShoppingBag),
        ToolItem("Herramientas audio", Icons.Default.VolumeUp),
        ToolItem("Avatares", Icons.Default.AccountCircle),
        ToolItem("Genera multimedia", Icons.Default.Folder),
        ToolItem("Todas (34)", Icons.Default.Apps)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (isPro) Color.Black else Color.White.copy(0.85f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.clickable { onOpenPaywall() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (isPro) "👑 CLIPCUT PRO ACTIVO" else "💎 Prueba Standard 7 días por $0",
                        fontSize = 12.sp,
                        color = if (isPro) ClipCutYellowCard else Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            IconButton(onClick = { Toast.makeText(context, "Buscador de efectos", Toast.LENGTH_SHORT).show() }) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            Text("Video create", fontSize = 13.sp, color = Color.Black.copy(0.75f), fontWeight = FontWeight.Medium)
            Text("Get started ›", fontSize = 28.sp, color = Color.Black, fontWeight = FontWeight.Black)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(95.dp)
                    .clickable { galleryLauncher.launch("video/*") },
                colors = CardDefaults.cardColors(containerColor = ClipCutYellowCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(28.dp))
                    Text("Video nuevo", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(95.dp)
                    .clickable { galleryLauncher.launch("image/*") },
                colors = CardDefaults.cardColors(containerColor = ClipCutYellowCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = Color.Black, modifier = Modifier.size(28.dp))
                    Text("Editar foto", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 8.dp),
            contentPadding = PaddingValues(vertical = 6.dp)
        ) {
            items(tools) { tool ->
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { galleryLauncher.launch("*/*") },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(tool.icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        tool.title,
                        fontSize = 11.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

// ----------------- PANTALLA 3: PLANTILLAS -----------------
@Composable
fun TemplatesScreen(onTemplateSelected: () -> Unit) {
    val templates = listOf(
        "Velocidad Automática & Zoom 3D" to "Estilo Viral TikTok",
        "Efecto Neón Cyberpunk" to "Especial 4K",
        "Recorte Profesional & Chroma" to "Fondo Verde IA",
        "Transición Rápida Beats" to "Sincronización Audio",
        "Filtro Retro VHS 90s" to "Nostalgia Vibes"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Plantillas Populares", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.Black)
        Text("Crea videos en un toque con estilos listos", fontSize = 13.sp, color = Color.Black.copy(0.7f))
        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(templates.size) { i ->
                val item = templates[i]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable { onTemplateSelected() },
                    colors = CardDefaults.cardColors(containerColor = ClipCutYellowCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Surface(color = Color.Black, shape = RoundedCornerShape(4.dp)) {
                                Text(" POPULAR ", color = ClipCutYellowCard, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(mod
