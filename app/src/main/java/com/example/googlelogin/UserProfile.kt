package com.example.googlelogin

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.util.concurrent.Executors

class UserProfile : AppCompatActivity(), View.OnClickListener {


    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        signout_btn.setOnClickListener(this);

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)

        if (acct != null) {

            // Declaring executor to parse the URL
            val executor = Executors.newSingleThreadExecutor()

            // Once the executor parses the URL
            // and receives the image, handler will load it
            // in the ImageView
            val handler = Handler(Looper.getMainLooper())

            // Initializing the image
            var image: Bitmap? = null

            // Only for Background process (can take time depending on the Internet speed)
            executor.execute {

                // Image URL
                val imageURL = acct.photoUrl.toString()

                // Tries to get the image and post it in the ImageView
                // with the help of Handler
                try {
                    val `in` = java.net.URL(imageURL).openStream()
                    image = BitmapFactory.decodeStream(`in`)

                    // Only for making changes in UI
                    handler.post {
                        Profileimage.setImageBitmap(image)
                    }
                }

                // If the URL doesnot point to
                // image or any other kind of failure
                catch (e: Exception) {
                    e.printStackTrace()
                }
                val personName = acct.displayName
                person_name.text = personName
                val personEmail = acct.email
                person_email.text = personEmail

            }
        }
    }

    override fun onClick(p0: View?) {
        signOut()
    }

    fun signOut() {
        mGoogleSignInClient?.signOut()
            ?.addOnCompleteListener(this, object : OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    Toast.makeText(this@UserProfile, "Signed Out", Toast.LENGTH_LONG).show()
                }
            })

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Really Exit?")
            .setMessage("Are you sure you want to exit?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
                override fun onClick(arg0: DialogInterface?, arg1: Int) {
                    finishAffinity()
                }
            }).create().show()
    }
}