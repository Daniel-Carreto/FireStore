package com.danycarreto.firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.danycarreto.firestore.adapter.Estado
import com.danycarreto.firestore.adapter.EstadosAdapter
import kotlinx.android.synthetic.main.activity_extra.*

class ExtraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra)

        println("Micurp: CACD930418HDFSRN03 es válida: ${"CACD930418HDFSRN03".isCurpValidate()}")
        println("Micurp: CACD93041HDFSRN03 es válida: ${"CACD93041HDFSRN03".isCurpValidate()}")

        ivExtension.loadUrl("https://1.bp.blogspot.com/-dwL58chu7wo/WvD1RrHln3I/AAAAAAAAFUg/cRTc0IZga_wMPTWr3CI53IZ5BwtnZMeYACLcBGAs/s1600/Screen%2BShot%2B2018-05-05%2Bat%2B11.49.30%2BAMimage1.png")

        //Extension de lista
        val nombres = arrayListOf("Ana","Carlos","Pablo","Daniel")
        println("Nombre: ${nombres[nombres.penultimoItem]}")

        //inflateSpinner()
        //showListView()

        showSpinnerCustom()
        showListViewCustom()
    }

    override fun onResume() {
        super.onResume()
        if(isNetworkAvailable()){
            Toast.makeText(this, "Red Disponible", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Red No Disponible", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Un ejemplo de como inflar un spinner sencillo con un layout propio de android.
     */
    fun inflateSpinner(){
        val postres = arrayOf("Cupcake","Donut","Eclair", "Froyo")
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, postres)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(baseContext, postres[position],Toast.LENGTH_SHORT).show()
            }

        }
    }


    /**
     * Ejemplo de el llenado de una listView sencillo con un layout propio de android
     */
    fun showListView(){
        val estados = resources.getStringArray(R.array.list_states)
        val adapter = ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, estados)
        lvStates.adapter = adapter
        lvStates.onItemClickListener = object: AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(baseContext, estados[position], Toast.LENGTH_SHORT).show()
            }
        }
    }


    //ListViewCustom
    //Creamos una clase Estado para inflar nuestro listview a partir de ese modelo
    //Para un listView custom, necesitamos crear nuestro propio Adapter, a partir del BaseActivity
    private fun showListViewCustom() {
        val listaEstaods = arrayListOf<Estado>(
            Estado("Guerrero", "Chilpancingo"),
            Estado("Jalisco", "Guadalajara")
        )

        val adapter = EstadosAdapter(listaEstaods)
        lvStates.adapter = adapter
        lvStates.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(
                this, "Capital: " + listaEstaods[position].capital,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    /**
     * Para el caso del spinner con el mismo adapter del listview lo podemos personalizar
     */
    private fun showSpinnerCustom(){
        val listaEstaods = arrayListOf<Estado>(
            Estado("Guerrero","Chilpancingo"),
            Estado("Jalisco","Guadalajara")
        )

        val adapter = EstadosAdapter(listaEstaods)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ){
                Toast.makeText(getBaseContext(), "---->Postre" + listaEstaods[position].capital,
                    Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("Nada")
            }

        }
    }


}
