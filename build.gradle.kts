// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    
//    detekt
    alias(libs.plugins.detekt) apply false

//    KSP
    alias(libs.plugins.ksp) apply false

//    Hilt
    alias(libs.plugins.hilt) apply false
}