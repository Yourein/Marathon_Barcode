package com.example.marathon_barcode.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun StatusScreenRoot(
    barcodeData: String,
    hp: Int,
    attack: Int,
    defence: Int,
    onBackToHomeClicked: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "君の見つけた\nバーコードは…",
            style = LocalTextStyle.current.merge(
                TextStyle(
                    lineHeight = 1.5.em,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    ),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 55.sp
                )
            )
        )

        Spacer(modifier = Modifier.height(55.dp))

        Text(text = "HP : $hp", fontSize = 32.sp)
        Text("ATK: $attack", fontSize = 32.sp)
        Text("DEF: $defence", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(55.dp))

        Button(onClick = onBackToHomeClicked) {
            Text("ホームへ戻る", fontSize = 32.sp)
        }
    }
}