package com.alltrails.lunch.uicore

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.alltrails.lunch.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

// This is from a personal project, copy pasta
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permissions(
    permissions: List<String>,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit = {},
) {
    val permissionsState = rememberMultiplePermissionsState(permissions)

    if (!permissionsState.allPermissionsGranted) {
        val requireSettings by remember {
            derivedStateOf {
                permissionsState.revokedPermissions.any { p -> p.status.shouldShowRationale }
            }
        }

        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = stringResource(R.string.permissionsRequired))
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (requireSettings) {
                            context.startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                },
                            )
                        } else {
                            permissionsState.launchMultiplePermissionRequest()
                        }
                    },
                ) {
                    Text(
                        text = stringResource(if (requireSettings) R.string.openSettings else R.string.ok),
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
        )
    } else {
        content()
    }
}
