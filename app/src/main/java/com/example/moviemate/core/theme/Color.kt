package com.example.moviemate.core.theme

import androidx.compose.ui.graphics.Color

/**
 * Material 3 color tokens.
 *
 * The palette is inspired by classic cinema: deep red accents (think theater
 * curtains and Netflix-esque branding), gold for ratings/highlights, and
 * dark neutrals for the cinema-like dark mode.
 *
 * Naming convention follows Material 3:
 *   - Primary, OnPrimary, PrimaryContainer, OnPrimaryContainer
 *   - Same for Secondary, Tertiary, etc.
 *   - "On*" colors are content drawn ON TOP of the base color
 */

// ----- Brand Reds -----
val CinemaRed = Color(0xFFE50914)            // Bold poster red
val CinemaRedLight = Color(0xFFFF4757)
val CinemaRedDark = Color(0xFFB30710)

// ----- Brand Golds (for ratings, accents) -----
val Gold = Color(0xFFFFC107)
val GoldDark = Color(0xFFC79100)

// ----- Light Theme -----
val md_theme_light_primary = CinemaRed
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFFFDAD6)
val md_theme_light_onPrimaryContainer = Color(0xFF410002)

val md_theme_light_secondary = Color(0xFF775652)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFFFDAD6)
val md_theme_light_onSecondaryContainer = Color(0xFF2C1512)

val md_theme_light_tertiary = Gold
val md_theme_light_onTertiary = Color(0xFF000000)
val md_theme_light_tertiaryContainer = Color(0xFFFFE082)
val md_theme_light_onTertiaryContainer = Color(0xFF231B00)

val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onErrorContainer = Color(0xFF410002)

val md_theme_light_background = Color(0xFFFFFBFF)
val md_theme_light_onBackground = Color(0xFF201A19)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF201A19)
val md_theme_light_surfaceVariant = Color(0xFFF5DDDA)
val md_theme_light_onSurfaceVariant = Color(0xFF534341)
val md_theme_light_outline = Color(0xFF857371)
val md_theme_light_outlineVariant = Color(0xFFD8C2BE)

// ----- Dark Theme (cinema-mode) -----
val md_theme_dark_primary = CinemaRedLight
val md_theme_dark_onPrimary = Color(0xFF690004)
val md_theme_dark_primaryContainer = Color(0xFF93000A)
val md_theme_dark_onPrimaryContainer = Color(0xFFFFDAD6)

val md_theme_dark_secondary = Color(0xFFE7BDB7)
val md_theme_dark_onSecondary = Color(0xFF442925)
val md_theme_dark_secondaryContainer = Color(0xFF5D3F3B)
val md_theme_dark_onSecondaryContainer = Color(0xFFFFDAD6)

val md_theme_dark_tertiary = Gold
val md_theme_dark_onTertiary = Color(0xFF3C2F00)
val md_theme_dark_tertiaryContainer = Color(0xFF564500)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFE082)

val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)

val md_theme_dark_background = Color(0xFF0F0E12)
val md_theme_dark_onBackground = Color(0xFFEDE0DE)
val md_theme_dark_surface = Color(0xFF1A1A1F)
val md_theme_dark_onSurface = Color(0xFFEDE0DE)
val md_theme_dark_surfaceVariant = Color(0xFF534341)
val md_theme_dark_onSurfaceVariant = Color(0xFFD8C2BE)
val md_theme_dark_outline = Color(0xFFA08C8A)
val md_theme_dark_outlineVariant = Color(0xFF534341)
