package com.example.mobilevideostream
import android.app.AlertDialog
import android.content.Intent
import com.example.mobilevideostream.DataBaseActions
import java.sql.*
import android.widget.Button
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilevideostream.ui.theme.MobileVideoStreamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var adresaEmail: EditText = findViewById(R.id.adresaEmail)
        var error:String?=null
        var parola: EditText = findViewById(R.id.parolaPlain)
        var butonLogIn: Button = findViewById(R.id.butonLogin)
        var butonSignUp: Button = findViewById(R.id.butonSignUp)
        var butonForgPass: Button = findViewById(R.id.butonForgPass)
        var rezultatInterogare:ResultSet?=null
        val actiuniDB= DataBaseActions() //creem un obiect de tip Databaseactions
        butonLogIn.setOnClickListener {
            val userMail = adresaEmail.text;
            val parolaForm = parola.text;
            if (adresaEmail.toString().isEmpty()) {
                error = "Email must not be empty"
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
                error=null
            } else if (!Patterns.EMAIL_ADDRESS.matcher(adresaEmail.toString()).matches()) {
                error = "Email is invalid"
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
                error=null
            } else {
                Toast.makeText(this@MainActivity, userMail, Toast.LENGTH_LONG).show() //asta e aici mai mult ca sa ne aratae ca ia ceva argumente
                actiuniDB.connect()
                rezultatInterogare = actiuniDB.checkUser(userMail.toString())
                rezultatInterogare?.next() //pentru a elimina randul ce contine numele coloanelor
                var userExista: Int? = rezultatInterogare?.getInt("exista")
                actiuniDB.disconnect()
                if (userExista == 1) { //daca utiizatorul exista in baza de date adica emailul e folosit
                    actiuniDB.connect()
                    rezultatInterogare = actiuniDB.getUserDetails(userMail.toString())
                    val sare: String? = rezultatInterogare?.getString("passSalt")
                    var parolaDB: String? = rezultatInterogare?.getString("passUtil")
                    var parolaHashuit: String? = actiuniDB.hashPass(parolaForm.toString(), sare)
                    actiuniDB.disconnect()
                    if (parolaDB.equals(parolaHashuit)) {
                        var ID: Int? = rezultatInterogare?.getInt("idUtilizator")
                        val intent = Intent(
                            this,
                            UserHomeActivity::class.java
                        ) //ar trebui sa ma trimita la cealalta activitate
                        intent.putExtra("userID", ID)
                        startActivity(intent)
                    }

                }


                //aici incercam sa il validam mai mult sa vad ca merge butonul
                // trebuie sa apelez o functie de validare care apoi sa puna ca valoare globala ID-ul utilizatorului
                // si poi sa il duca in activitatea de HOME

            }
        }
        butonSignUp.setOnClickListener {
        // daca apasa redirectioneaza catre activitatea de Sign-Up care apoi va redirectiona catre MAIN
            val intent =Intent(this, UserSignUpActivity::class.java) //ar trebui sa ma trimita la cealalta activitate
            startActivity(intent)
        }
        butonForgPass.setOnClickListener {
        // similar apasa redirectioneaza catre activitate de Forgpass apoi la cea de MAIN
            val intent =Intent(this, UserForgPassActivity::class.java) //ar trebui sa ma trimita la cealalta activitate
            startActivity(intent)
        }
    }
}
/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileVideoStreamTheme {
        Greeting("Android")
    }
}
*/