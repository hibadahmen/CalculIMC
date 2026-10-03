package com.example.calculimc

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Empêche le contenu de passer sous les barres du téléphone.
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Récupérer les composants de l'interface.
        val editTextPoids =
            findViewById<EditText>(R.id.editTextPoids)

        val editTextTaille =
            findViewById<EditText>(R.id.editTextTaille)

        val buttonCalculer =
            findViewById<Button>(R.id.buttonCalculer)

        val buttonEffacer =
            findViewById<Button>(R.id.buttonEffacer)

        val textViewImc =
            findViewById<TextView>(R.id.textViewImc)

        val textViewCategorie =
            findViewById<TextView>(R.id.textViewCategorie)

        // Action du bouton Calculer.
        buttonCalculer.setOnClickListener {

            // Effacer les anciens messages et résultats.
            editTextPoids.error = null
            editTextTaille.error = null
            textViewImc.text = ""
            textViewCategorie.text = ""

            val poids = lireValeurPositive(editTextPoids)
            val taille = lireValeurPositive(editTextTaille)

            if (poids == null || taille == null) {
                if (poids == null) {
                    editTextPoids.requestFocus()
                } else {
                    editTextTaille.requestFocus()
                }

                return@setOnClickListener
            }

            // Calculer l'IMC.
            val imc = poids / (taille * taille)

            if (!imc.isFinite() || imc <= 0.0) {
                Toast.makeText(
                    this,
                    R.string.erreur_calcul,
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            val categorie: Int
            val couleur: Int

            if (imc < 18.5) {
                categorie = R.string.categorie_insuffisance
                couleur = Color.rgb(180, 90, 0)

            } else if (imc < 25.0) {
                categorie = R.string.categorie_normale
                couleur = Color.rgb(0, 120, 50)

            } else if (imc < 30.0) {
                categorie = R.string.categorie_surpoids
                couleur = Color.rgb(180, 90, 0)

            } else if (imc < 35.0) {
                categorie = R.string.categorie_moderee
                couleur = Color.rgb(190, 0, 0)

            } else if (imc < 40.0) {
                categorie = R.string.categorie_severe
                couleur = Color.rgb(190, 0, 0)

            } else {
                categorie = R.string.categorie_morbide
                couleur = Color.rgb(120, 0, 0)
            }


            val imcFormate = String.format(
                Locale.FRANCE,
                "%.2f",
                imc
            )

            textViewImc.text = getString(
                R.string.resultat_imc,
                imcFormate
            )

            textViewCategorie.setText(categorie)

            textViewImc.setTextColor(couleur)
            textViewCategorie.setTextColor(couleur)
        }

        // Action du bouton Effacer.
        buttonEffacer.setOnClickListener {
            editTextPoids.text.clear()
            editTextTaille.text.clear()

            editTextPoids.error = null
            editTextTaille.error = null

            textViewImc.text = ""
            textViewCategorie.text = ""

            editTextPoids.requestFocus()
        }
    }


    private fun lireValeurPositive(champ: EditText): Double? {

        val texte = champ.text.toString().trim()

        if (texte.isEmpty()) {
            champ.error = getString(R.string.erreur_vide)
            return null
        }
        val valeur = texte.replace(',', '.').toDoubleOrNull()

        if (valeur == null || !valeur.isFinite()) {
            champ.error = getString(R.string.erreur_nombre)
            return null
        }

        if (valeur <= 0.0) {
            champ.error = getString(R.string.erreur_positive)
            return null
        }

        return valeur
    }
}