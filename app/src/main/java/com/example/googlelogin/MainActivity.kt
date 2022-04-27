package com.example.googlelogin

import android.accessibilityservice.GestureDescription
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user_profile.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    /* Google Play services requires us to provide the SHA-1 of our signing certificate so Google can
    create an OAuth2 client and API key for our app.click on Configure a project(link in ring).
    Enter project name, select Andorid, enter package name and SHA-1 certificate.*/

    private val RC_SIGN_IN = 1 //DEFAULT_SIGN_IN parameter

    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)//To request users' email
                // addresses create the GoogleSignInOptions object with the requestEmail option.
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


    }

    override fun onStart() {
        super.onStart()
        val account =
            GoogleSignIn.getLastSignedInAccount(this)//check if a user has already signed in
        // to your app with Google

        if (account != null) {
            val intent = Intent(this@MainActivity, UserProfile::class.java)
            startActivity(intent)//If account != null, the user has already signed in to your app
        // with Google. Launch your UserProfile activity using intent
        } else {
            sign_in_button.setOnClickListener(this)//If account == null, the user has not yet signed
        // in to your app with Google. Now the user has to click on Google Sign-in button for signing
        // in. Handle sign-in button taps on sign-in activity.
        }

    }

    override fun onClick(p0: View?) {
        signIn()
    }

    private fun signIn() {
        //By calling signIn()we launch the login screen using the Intent we get by calling
        // getSignInIntent method on the GoogleSignInClient object, and start the login by calling
        // startActivityForResult
        val intent =
            mGoogleSignInClient!!.signInIntent//The intent prompts the user to select a Google
        // account to sign in with.
        startActivityForResult(intent, RC_SIGN_IN)
    }

    //The GoogleSignInAccount object (account) contains information about the signed-in user,
    // such as the user's name ,email etc
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            //    progress_loader.visibility = View.VISIBLE
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)

                val intent = Intent(this@MainActivity, UserProfile::class.java)
                startActivity(intent)

            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.e("TAG", "signInResult:failed code=" + e.statusCode)
            }
        }
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


