package github.masterj3y.subtitle.ui.lottie

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView

@Composable
fun Lottie(
    modifier: Modifier = Modifier,
    @RawRes animation:Int,
    play:Boolean = true,
    loop:Boolean = true,
    speed:Float =1f
) {
    AndroidView(modifier=modifier,factory = { LottieAnimationView(it) }){
        it.setAnimation(animation)
        it.loop(loop)
        it.speed = speed
        if (play) it.playAnimation() else it.pauseAnimation()
    }
}