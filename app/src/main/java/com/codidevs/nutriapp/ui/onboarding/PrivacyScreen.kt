package com.codidevs.nutriapp.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codidevs.nutriapp.BuildConfig
import com.codidevs.nutriapp.R
import com.codidevs.nutriapp.ui.components.ScreenHeader
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.LeafDark

@Composable
fun PrivacyScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        ScreenHeader(titulo = "Privacidad", onBack = onBack)
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.privacy_policy_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Copia pública",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = LeafDark
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = BuildConfig.PRIVACY_URL,
            style = MaterialTheme.typography.bodySmall,
            color = InkSoft,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
    }
}
