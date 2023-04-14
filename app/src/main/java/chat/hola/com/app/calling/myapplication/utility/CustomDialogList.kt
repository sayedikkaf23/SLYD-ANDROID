package com.appscrip.myapplication.utility

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Annotation
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import com.ezcall.android.R
import kotlinx.android.synthetic.main.dialog_sign_up.*

/**
 * @author 3Embed
 *
 * @see https://medium.com/@kirillsuslov/how-to-add-link-inside-text-in-android-f5bd50c03dbe for builder pattern
 *@see https://android.jlelse.eu/create-complex-object-at-run-time-with-builder-pattern-d425e6f4408e for builder pattern
 * @see https://medium.com/@vicidroiddev/using-builders-in-kotlin-data-class-e8a08797ed56 for spannable string
 *
 *
 * @property using custom alert dialog
 *
 */
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CustomDialogList(
    context: Context,
    private var isCancel: Boolean,
    private var title: String?,
    private var closeListener: OnClickCloseListener,
    private var signUpListener: OnClickSignUpListener
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_sign_up)
        this.window?.setBackgroundDrawableResource(R.drawable.dialog_round_corner)
        /** Annotations are to add metadata to a class and spannable string lets to style a substring.*/
        val fullText = context.getText(R.string.dialog_sign_up_text) as SpannedString
        val spannableString = SpannableString(fullText)
        val annotations = fullText.getSpans(0, fullText.length, Annotation::class.java)
        // handle click on close button
        ivDialogSignUp_cancel.setOnClickListener {
            closeListener.onClickClose(this)
        }
        // click on signUp bu
        val clickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View?) {
//                signUpListener.onSignUpClick(this@CustomDialogList)
//            }
//
//            override fun updateDrawState(ds: TextPaint?) {
//                ds?.isUnderlineText = false
//            }

            override fun onClick(widget: View) {
                signUpListener.onSignUpClick(this@CustomDialogList)
            }
        }
        // to find spanned string and modify that
        annotations?.find { it.value == "sign_up_link" }?.let {
            spannableString.apply {
                setSpan(
                    clickableSpan,
                    fullText.getSpanStart(it),
                    fullText.getSpanEnd(it),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(context, R.color.colorAccent)
                    ),
                    fullText.getSpanStart(it),
                    fullText.getSpanEnd(it),
                    0
                )
            }
        }

        tvDialogSignUp_text.apply {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun setCancelable(cancelable: Boolean) {
        this.isCancel = cancelable
        super.setCancelable(cancelable)
    }

    interface OnClickCloseListener {
        fun onClickClose(dialog: CustomDialogList)
    }

    interface OnClickSignUpListener {
        fun onSignUpClick(dialog: CustomDialogList)
    }

    class Builder(private var context: Context) {
        private var closeListener: OnClickCloseListener? = null
        private var title: String? = null
        private var isCancel: Boolean = true
        private var signUpListener: OnClickSignUpListener? = null

        fun withTitle(title: String): Builder {
            this.title = title
            return this
        }

        /**Configures click on cancel */
        fun setOnCloseClick(closeListener: OnClickCloseListener): Builder {
            this.closeListener = closeListener
            return this
        }

        /**Configures click on signUp */
        fun onSignUpClick(signUpListener: OnClickSignUpListener): Builder {
            this.signUpListener = signUpListener
            return this
        }

        /** Configures whether or not the dialog can be cancelled. */
        fun cancelable(isCancel: Boolean): Builder {
            this.isCancel = isCancel
            return this
        }

        fun show() =
                CustomDialogList(context, isCancel, title, closeListener!!, signUpListener!!).show()
    }
}