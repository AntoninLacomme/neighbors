package fr.mbds.android.neighbors.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputEditText
import fr.mbds.android.neighbors.NavigationListener
import fr.mbds.android.neighbors.R
import fr.mbds.android.neighbors.data.NeighborRepository
import fr.mbds.android.neighbors.models.Neighbor

class AddNeighborFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? NavigationListener)?.let {
            it.updateTitle(R.string.title_add_neighbors)
        }

        val view = inflater.inflate(R.layout.add_neighbor_fragment, container, false)
        val inputImage = view?.findViewById<TextInputEditText>(R.id.textFieldImage)
        val inputNom = view?.findViewById<TextInputEditText>(R.id.textFieldNom)
        val inputTelephone = view?.findViewById<TextInputEditText>(R.id.textFieldTelephone)
        val inputWebsite = view?.findViewById<TextInputEditText>(R.id.textFieldWebsite)
        val inputAddresse = view?.findViewById<TextInputEditText>(R.id.textFieldAdresse)
        val inputProps = view?.findViewById<TextInputEditText>(R.id.textFieldApropos)
        val buttonValidate = view?.findViewById<Button>(R.id.buttonRegister)

        var imgReady = false;
        var nomReady = false;
        var telReady = false;
        var webReady = false;
        var adrReady = false;
        var prpReady = true;

        fun setEnableButton () {
            buttonValidate?.isEnabled = imgReady && nomReady && telReady && webReady && adrReady && prpReady
        }

        fun checkInputImage (): Boolean {
            return URLUtil.isValidUrl(inputImage?.getText().toString())
        }

        fun checkInputNom (): Boolean {
            return inputNom?.getText()?.length!! >= 3;
        }

        fun checkInputTelephone (): Boolean {
            val tel = inputTelephone?.getText().toString()
            var tr = tel.length!! == 10;
            tr = tr && tel[0] == '0'
            return tr && (tel[1] == '6' || tel[1] == '7')
        }

        fun checkInputWebSite (): Boolean {
            return URLUtil.isValidUrl(inputWebsite?.getText().toString())
        }

        fun checkInputAdresse (): Boolean {
            return inputAddresse?.getText()?.length!! > 0
        }

        fun checkInputApropos (): Boolean {
            return inputProps?.getText()?.length!! <= 30
        }

        fun checkAll (): Boolean {
            return checkInputImage () &&
                    checkInputNom () &&
                    checkInputTelephone () &&
                    checkInputWebSite () &&
                    checkInputAdresse () &&
                    checkInputApropos ()
        }

        inputImage?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) {
                imgReady = checkInputImage()
                if (!imgReady)
                    inputImage.setError(context?.resources?.getString(R.string.err_inscription_image))
                if (imgReady) {
                    val labelImage = view?.findViewById<ImageView>(R.id.imageView);
                    if (labelImage != null) {
                        val url = inputImage?.getText().toString();
                        Glide.with(view.context)
                            .load(url)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(R.drawable.ic_baseline_person_outline_24)
                            .error(R.drawable.ic_baseline_error_outline_24)
                            .skipMemoryCache(false)
                            .into(labelImage)
                    }


                }
                setEnableButton ()
            }
        })
        inputNom?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) {
                nomReady = checkInputNom ()
                if (!nomReady)
                    inputNom.setError(context?.resources?.getString(R.string.err_inscription_nom))
                setEnableButton ()
            }
        })
        inputTelephone?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) {
                telReady = checkInputTelephone ()
                if (!telReady)
                    inputTelephone.setError(context?.resources?.getString(R.string.err_inscription_telephone))
                setEnableButton ()
            }
        })
        inputWebsite?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) {
                webReady = checkInputWebSite ()
                if (!webReady)
                    inputWebsite.setError(context?.resources?.getString(R.string.err_inscription_siteweb))
                setEnableButton ()
            }
        })
        inputAddresse?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) {
                adrReady = checkInputAdresse ()
                if (!adrReady)
                    inputAddresse.setError(context?.resources?.getString(R.string.err_inscription_adresse))
                setEnableButton ()
            }
        })
        inputProps?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) {
                prpReady = checkInputApropos ()
                if (!prpReady)
                    inputProps.setError(context?.resources?.getString(R.string.err_inscription_apropos))
                setEnableButton ()
            }
        })

        buttonValidate?.setOnClickListener {
            if (checkAll()) {
                val neighbors = NeighborRepository.getInstance().getNeighbours()
                neighbors.add(Neighbor (neighbors.size.toLong(),
                    inputNom?.getText().toString(),
                    inputImage?.getText().toString(),
                    inputAddresse?.getText().toString(),
                    inputTelephone?.getText().toString(),
                    inputProps?.getText().toString(),
                    false,
                    inputWebsite?.getText().toString()
                ))

                (activity as? NavigationListener)?.let {
                    it.showFragment(ListNeighborsFragment())
                }
            }
        }
        return view
    }
}
