package com.put.polishparliamentapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.animation.Animator

class AnimationFragment : Fragment() {

    private lateinit var mWhiteRect: View
    private lateinit var mRedRect: View

    interface Listener {
        fun endOfAnimation()
    }

    private lateinit var listener: Listener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.animation_fragmet, container, false)
        mWhiteRect = view.findViewById(R.id.white)
        mRedRect = view.findViewById(R.id.red)

        val observer = view.viewTreeObserver
        observer.addOnGlobalLayoutListener {
            startAnimation()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =  AnimationFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as Listener
    }

    private fun startAnimation() {
        val yStart = mWhiteRect.top.toFloat()
        val yEndRed = yStart / 2
        val yEndWhite = yEndRed - mWhiteRect.height
        Log.i("y", yStart.toString())
        val whiteAnimator = ObjectAnimator.ofFloat(mWhiteRect, "y", yStart, yEndWhite).setDuration(1000)
        val redAnimator = ObjectAnimator.ofFloat(mRedRect, "y", yStart, yEndRed).setDuration(1000)

        whiteAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator) {
                listener.endOfAnimation()
            }
            override fun onAnimationStart(p0: Animator) {}
            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
        })

        val animatorSet = AnimatorSet()
        animatorSet.play(whiteAnimator).with(redAnimator)

        animatorSet.start()
    }
}