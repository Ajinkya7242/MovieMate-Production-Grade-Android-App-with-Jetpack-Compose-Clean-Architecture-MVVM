package com.example.moviemate.core.components

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer as YouTubePlayerInterface
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayer(
    videoId: String,
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val decorView = remember { activity?.window?.decorView as? ViewGroup }

    var isReady by remember { mutableStateOf(false) }
    var player by remember { mutableStateOf<YouTubePlayerInterface?>(null) }
    // Holds the fullscreen view added to decorView so we can remove it later
    val fsViewState = remember { mutableStateOf<View?>(null) }

    val playerView = remember {
        YouTubePlayerView(context).apply {
            enableAutomaticInitialization = false

            addFullscreenListener(object : FullscreenListener {
                override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                    // Must add the library-provided view to cover the entire screen
                    fsViewState.value = fullscreenView
                    decorView?.addView(
                        fullscreenView,
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )
                    @Suppress("DEPRECATION")
                    decorView?.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }

                override fun onExitFullscreen() {
                    fsViewState.value?.let { decorView?.removeView(it) }
                    fsViewState.value = null
                    @Suppress("DEPRECATION")
                    decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    // Back to portrait once user exits fullscreen
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            })

            val options = IFramePlayerOptions.Builder(context)
                .controls(1)
                .fullscreen(1)
                .rel(0)
                .build()

            initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayerInterface) {
                    player = youTubePlayer
                    isReady = true
                    // LaunchedEffect below handles the actual load
                }
            }, options)
        }
    }

    // Load video once player is ready, and reload if videoId changes
    LaunchedEffect(isReady, videoId) {
        if (isReady) {
            player?.loadVideo(videoId, 0f)
        }
    }

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(playerView)
        onDispose {
            // Remove fullscreen overlay if still showing
            fsViewState.value?.let { decorView?.removeView(it) }
            @Suppress("DEPRECATION")
            decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            // Always return to portrait when the player is closed
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            lifecycleOwner.lifecycle.removeObserver(playerView)
            playerView.release()
        }
    }

    Box(modifier = modifier.background(Color.Black)) {
        AndroidView(
            factory = { playerView },
            modifier = Modifier.fillMaxSize()
        )
        // Show spinner while the WebView/player is still initialising
        if (!isReady) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }
    }
}
