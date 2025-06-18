package com.quix

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import android.net.Uri
import com.quix.ui.FolderGridView
import com.quix.ui.ImageGridView
import com.quix.ui.FullscreenView

@Composable
fun Navigation(navController: NavHostController) {
  NavHost(
    navController = navController,
    startDestination = "folder_grid_view"
  ) {

    composable("folder_grid_view",
      exitTransition = {
        slideOutHorizontally(
          targetOffsetX = {
            -it
          }, // Slide out to the left
          animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
          )
        )
      },
      popEnterTransition = {
        slideInHorizontally(
          initialOffsetX = {
            -it
          }, // Slide in from the left
          animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
          )
        )
      }
    ) {
      FolderGridView(onFolderClick = {
        bucketId -> navController.navigate("image_grid_view/$bucketId") {
          launchSingleTop = true
        }
      })
    }

    composable(
      route = "image_grid_view/{itemArg}",
      arguments = listOf(navArgument("itemArg") {
        type = NavType.StringType
      }),
      enterTransition = {
        slideInHorizontally(
          initialOffsetX = {
            it
          }, // Slide in from the right
          animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
          )
        )
      },
      popExitTransition = {
        slideOutHorizontally(
          targetOffsetX = {
            it
          }, // Slide out to the right
          animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
          )
        )
      }
    ) {
      backStackEntry ->
      ImageGridView(bucketId = backStackEntry.arguments?.getString("itemArg"), onImageClick = { imageUri ->
      val encoded = Uri.encode(imageUri)
        navController.navigate("full_view/$encoded")
      })
    }

    composable(
      route = "full_view/{imageUri}",
      arguments = listOf(navArgument("imageUri") {
        type = NavType.StringType
      })
    ) {
      backStackEntry ->
      val imageUriString = backStackEntry.arguments?.getString("imageUri")!!
      FullscreenView(imageUriString)
    }



  }
}