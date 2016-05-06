package co.emes.esuelos.layout;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.emes.esuelos.R;
import co.emes.esuelos.forms.SkylineAdapter;
import co.emes.esuelos.model.Domain;
import co.emes.esuelos.model.Skyline;
import co.emes.esuelos.util.ActivityResultEvent;
import co.emes.esuelos.util.DataBaseHelper;
import co.emes.esuelos.util.Utils;

/**
 * Created by csarmiento on 11/14/16
 */
public class FragmentFieldNote extends DialogFragment implements View.OnClickListener {

    public static final String TAG = FragmentFieldNote.class.getSimpleName();

    TabHost host;
    EditText inputNroObservacion;
    EditText inputLayoutFecha;
    Spinner inputReconocedor;
    Spinner inputEpoca;
    Spinner inputLongitud;
    Spinner inputErosion;
    Spinner inputMovimiento;
    Spinner inputAnegamiento;
    TextView labelFrecuencia;
    Spinner inputFrecuencia;
    TextView labelDuracion;
    Spinner inputDuracion;

    ImageView imgViewPic;
    TextView labelHorizonteNro;
    EditText inputDepth;
    Spinner inputColorHue;
    Spinner inputColorValue;
    Spinner inputColorChroma;
    EditText inputColorPercent;
    Spinner inputMaterialType;
    TextView labelTexture;
    Spinner inputTexture;
    Spinner inputTextureModifiers;
    EditText inputTexturePercent;
    TextView labelOrganicType;
    Spinner inputOrganicType;
    Spinner inputOrganicType2;
    TextView labelTextureOther;
    EditText inputTextureOther;
    TextView labelStructure;
    Spinner inputStructure;
    Spinner inputStructureType;
    Spinner inputStructureType2;
    TextView labelStructureTypeOther;
    EditText inputStructureTypeOther;
    TextView labelStructureForm;
    Spinner inputStructureForm;
    TextView labelStructureReasons;
    Spinner inputStructureReasons;
    Spinner inputSkylineType;
    LinearLayout linearLayoutOpt;
    Spinner inputSoilFragment;
    Spinner inputNaturalDrainage;
    Spinner inputEffectiveDepth;
    Spinner inputEpidedones;
    Spinner inputEndopedones;

    Button btnPreview;

    List<Domain> listStructureType;

    ArrayAdapter<Domain> colorHueAdapter;
    ArrayAdapter<Domain> colorValueAdapter;
    ArrayAdapter<Domain> colorChromaAdapter;
    ArrayAdapter<Domain> materialTypeAdapter;
    ArrayAdapter<Domain> textureModifiersAdapter;
    ArrayAdapter<Domain> textureAdapter;
    ArrayAdapter<Domain> organicTypeAdapter;
    ArrayAdapter<Domain> organicTypeAdapter2;
    ArrayAdapter<Domain> structureAdapter;
    ArrayAdapter<Domain> structureTypeAdapter;
    ArrayAdapter<Domain> structureTypeAdapter2;
    ArrayAdapter<Domain> structureFormsAdapter;
    ArrayAdapter<Domain> structureReasonsAdapter;
    ArrayAdapter<Domain> skylineTypeAdapter;

    NestedScrollView scrollviewProfile;
    Skyline skylineObject;
    List<Skyline> skylineList;
    ListView listSkyline;
    Modes mode;
    int index;
    int indexOptional;
    int indexFlecked;
    List<View> viewList;

    private static final int RESULT_OK = -1;
    public static final int RESULT_LOAD_IMAGE = 1000;
    public static final int REQUEST_IMAGE_CAPTURE = 1001;

    enum Modes {
        NEW, EDIT
    }

    enum SkylineType {
        OPTIONAL, FLECKED
    }

    public FragmentFieldNote() {
        super();
        index = 0;
        indexOptional = 0;
        indexFlecked = 0;
        viewList = new LinkedList<>();
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.white)));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_field_note, container, false);
        getDialog().setCancelable(false);

        host = (TabHost)rootView.findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec(getResources().getString(R.string.tst_general));
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.tst_general));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec(getResources().getString(R.string.tst_externa));
        spec.setContent(R.id.tab2);
        spec.setIndicator(getResources().getString(R.string.tst_externa));
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec(getResources().getString(R.string.tst_profile));
        spec.setContent(R.id.tab3);
        spec.setIndicator(getResources().getString(R.string.tst_profile));
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec(getResources().getString(R.string.tst_horizontes));
        spec.setContent(R.id.tab4);
        spec.setIndicator(getResources().getString(R.string.tst_horizontes));
        host.addTab(spec);

        //Tab 5
        spec = host.newTabSpec(getResources().getString(R.string.tst_integral));
        spec.setContent(R.id.tab5);
        spec.setIndicator(getResources().getString(R.string.tst_integral));
        host.addTab(spec);

        imgViewPic  = (ImageView) rootView.findViewById(R.id.imgView_pic);

        inputNroObservacion = (EditText) rootView.findViewById(R.id.input_nro_observacion);
        inputLayoutFecha = (EditText) rootView.findViewById(R.id.input_fecha);
        inputReconocedor = (Spinner) rootView.findViewById(R.id.input_reconocedor);
        inputEpoca = (Spinner) rootView.findViewById(R.id.input_epoca);
        inputLongitud = (Spinner) rootView.findViewById(R.id.input_longitud);
        inputErosion = (Spinner) rootView.findViewById(R.id.input_erosion);
        inputMovimiento = (Spinner) rootView.findViewById(R.id.input_movimiento);
        inputAnegamiento = (Spinner) rootView.findViewById(R.id.input_anegamiento);
        labelFrecuencia = (TextView) rootView.findViewById(R.id.label_frecuencia);
        inputFrecuencia = (Spinner) rootView.findViewById(R.id.input_frecuencia);
        labelDuracion = (TextView) rootView.findViewById(R.id.label_duracion);
        inputDuracion = (Spinner) rootView.findViewById(R.id.input_duracion);

        labelHorizonteNro = (TextView) rootView.findViewById(R.id.label_horizonte_nro);
        inputDepth = (EditText) rootView.findViewById(R.id.input_profundidad);
        inputColorHue = (Spinner) rootView.findViewById(R.id.input_color_hue);
        inputColorValue = (Spinner) rootView.findViewById(R.id.input_color_value);
        inputColorChroma = (Spinner) rootView.findViewById(R.id.input_color_chroma);
        inputColorPercent = (EditText) rootView.findViewById(R.id.input_color_percent);
        inputMaterialType = (Spinner) rootView.findViewById(R.id.input_material_type);
        labelTexture = (TextView) rootView.findViewById(R.id.label_texture);
        inputTexture = (Spinner) rootView.findViewById(R.id.input_texture);
        labelTextureOther = (TextView) rootView.findViewById(R.id.label_texture_other);
        inputTextureOther = (EditText) rootView.findViewById(R.id.input_texture_other);
        inputTextureModifiers  = (Spinner) rootView.findViewById(R.id.input_texture_modifiers);
        inputTexturePercent = (EditText) rootView.findViewById(R.id.input_texture_percent);
        labelOrganicType = (TextView) rootView.findViewById(R.id.label_organic_type);
        inputOrganicType  = (Spinner) rootView.findViewById(R.id.input_organic_type);
        inputOrganicType2  = (Spinner) rootView.findViewById(R.id.input_organic_type2);
        labelStructure = (TextView) rootView.findViewById(R.id.label_structure);
        inputStructure = (Spinner) rootView.findViewById(R.id.input_structure);
        inputStructureType = (Spinner) rootView.findViewById(R.id.input_structure_type);
        inputStructureType2 = (Spinner) rootView.findViewById(R.id.input_structure_type2);
        labelStructureTypeOther = (TextView) rootView.findViewById(R.id.label_structure_type_other);
        inputStructureTypeOther = (EditText) rootView.findViewById(R.id.input_structure_type_other);
        inputSkylineType = (Spinner) rootView.findViewById(R.id.input_horizontes);
        labelStructureForm = (TextView) rootView.findViewById(R.id.label_structure_form);
        inputStructureForm = (Spinner) rootView.findViewById(R.id.input_structure_form);
        labelStructureReasons = (TextView) rootView.findViewById(R.id.label_structure_reasons);
        inputStructureReasons = (Spinner) rootView.findViewById(R.id.input_structure_reasons);

        inputSoilFragment = (Spinner) rootView.findViewById(R.id.input_soil_fragment);
        inputNaturalDrainage = (Spinner) rootView.findViewById(R.id.input_natural_drainage);
        inputEffectiveDepth = (Spinner) rootView.findViewById(R.id.input_effective_depth);
        inputEpidedones = (Spinner) rootView.findViewById(R.id.input_epidedones);
        inputEndopedones = (Spinner) rootView.findViewById(R.id.input_endopedones   );

        scrollviewProfile = (NestedScrollView) rootView.findViewById(R.id.scrollview_profile);
        listSkyline = (ListView) rootView.findViewById(R.id.list_skyline);

        Button btnSave = (Button) rootView.findViewById(R.id.btn_save);
        Button btnTakePhoto = (Button) rootView.findViewById(R.id.btn_take_photo);
        Button btnSelectPhoto = (Button) rootView.findViewById(R.id.btn_select_photo);
        Button btnOptional = (Button) rootView.findViewById(R.id.btn_optional);
        Button btnFlecked = (Button) rootView.findViewById(R.id.btn_add_flecked);
        Button btnNext = (Button) rootView.findViewById(R.id.btn_next);
        btnPreview = (Button) rootView.findViewById(R.id.btn_preview);

        btnSave.setTransformationMethod(null);
        btnTakePhoto.setTransformationMethod(null);
        btnSelectPhoto.setTransformationMethod(null);
        btnOptional.setTransformationMethod(null);
        btnFlecked.setTransformationMethod(null);
        btnNext.setTransformationMethod(null);
        btnNext.setTransformationMethod(null);
        btnPreview.setTransformationMethod(null);
        btnPreview.setEnabled(false);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent
                            .resolveActivity((getActivity())
                                    .getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent,
                                REQUEST_IMAGE_CAPTURE);
                    }
                } catch(ActivityNotFoundException exp){
                    Toast.makeText(getActivity(), "Problema al activar la cámara!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } catch(ActivityNotFoundException exp){
                    Toast.makeText(getActivity(), "No File (Manager / Explorer)etc Found In Your Device", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAction();
            }
        });

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewAction();
            }
        });

        linearLayoutOpt = (LinearLayout) rootView.findViewById(R.id.Buttons);
        btnOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOptionalView(inflater, SkylineType.OPTIONAL);

                /*int toolbarHeight = getActivity().findViewById(R.id.toolbar).getHeight();
                scrollviewProfile.startNestedScroll(View.SCROLL_AXIS_VERTICAL);
                scrollviewProfile.dispatchNestedPreScroll(0, toolbarHeight, null, null);
                scrollviewProfile.dispatchNestedScroll(0, 0, 0, 0, new int[]{0, -toolbarHeight});
                scrollviewProfile.scrollTo(0, scrollviewProfile.getBottom());*/
            }
        });

        btnFlecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOptionalView(inflater, SkylineType.FLECKED);

                /*int toolbarHeight = getActivity().findViewById(R.id.toolbar).getHeight();
                scrollviewProfile.startNestedScroll(View.SCROLL_AXIS_VERTICAL);
                scrollviewProfile.dispatchNestedPreScroll(0, toolbarHeight, null, null);
                scrollviewProfile.dispatchNestedScroll(0, 0, 0, 0, new int[]{0, -toolbarHeight});
                scrollviewProfile.scrollTo(0, scrollviewProfile.getBottom());*/
            }
        });

        inputNroObservacion.setText("C000001");
        inputLayoutFecha.setText(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        labelHorizonteNro.setText(String.format(getResources().getString(R.string.tst_horizonte_nro),(index + 1)));

        labelFrecuencia.setVisibility(View.GONE);
        labelDuracion.setVisibility(View.GONE);
        inputFrecuencia.setVisibility(View.GONE);
        inputDuracion.setVisibility(View.GONE);

        labelTexture.setVisibility(View.GONE);
        inputTexture.setVisibility(View.GONE);
        inputTextureModifiers.setVisibility(View.GONE);
        labelOrganicType.setVisibility(View.GONE);
        inputOrganicType.setVisibility(View.GONE);
        inputOrganicType2.setVisibility(View.GONE);
        labelTextureOther.setVisibility(View.GONE);
        inputTextureOther.setVisibility(View.GONE);
        labelStructure.setVisibility(View.GONE);
        inputStructure.setVisibility(View.GONE);
        inputStructureType.setVisibility(View.GONE);
        inputStructureType2.setVisibility(View.GONE);
        labelStructureTypeOther.setVisibility(View.GONE);
        inputStructureTypeOther.setVisibility(View.GONE);
        labelStructureForm.setVisibility(View.GONE);
        inputStructureForm.setVisibility(View.GONE);
        labelStructureReasons.setVisibility(View.GONE);
        inputStructureReasons.setVisibility(View.GONE);

        skylineList = new LinkedList<>();
        skylineObject =  new Skyline();
        mode = Modes.NEW;

        inputAnegamiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Domain domain = (Domain) inputAnegamiento.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (domain.getCodigo().equals("3")) {
                        labelFrecuencia.setVisibility(View.GONE);
                        labelDuracion.setVisibility(View.GONE);
                        inputFrecuencia.setVisibility(View.GONE);
                        inputDuracion.setVisibility(View.GONE);
                    } else {
                        labelFrecuencia.setVisibility(View.VISIBLE);
                        labelDuracion.setVisibility(View.VISIBLE);
                        inputFrecuencia.setVisibility(View.VISIBLE);
                        inputDuracion.setVisibility(View.VISIBLE);
                    }
                } else {
                    labelFrecuencia.setVisibility(View.GONE);
                    labelDuracion.setVisibility(View.GONE);
                    inputFrecuencia.setVisibility(View.GONE);
                    inputDuracion.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        inputMaterialType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Domain domain = (Domain) inputMaterialType.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (domain.getCodigo().equals("2")) {
                        labelTexture.setVisibility(View.GONE);
                        inputTexture.setVisibility(View.GONE);
                        inputTextureModifiers.setVisibility(View.GONE);
                        labelOrganicType.setVisibility(View.VISIBLE);
                        inputOrganicType.setVisibility(View.VISIBLE);
                        inputOrganicType2.setVisibility(View.VISIBLE);
                        labelStructure.setVisibility(View.GONE);
                        inputStructure.setVisibility(View.GONE);
                        inputStructureType.setVisibility(View.GONE);
                        inputStructureType2.setVisibility(View.GONE);
                    } else {
                        labelTexture.setVisibility(View.VISIBLE);
                        inputTexture.setVisibility(View.VISIBLE);
                        inputTextureModifiers.setVisibility(View.VISIBLE);
                        labelOrganicType.setVisibility(View.GONE);
                        inputOrganicType.setVisibility(View.GONE);
                        inputOrganicType2.setVisibility(View.GONE);
                        labelStructure.setVisibility(View.VISIBLE);
                        inputStructure.setVisibility(View.VISIBLE);
                        inputStructureType.setVisibility(View.VISIBLE);
                        inputStructureType2.setVisibility(View.VISIBLE);
                    }
                } else {
                    labelTexture.setVisibility(View.GONE);
                    inputTexture.setVisibility(View.GONE);
                    inputTextureModifiers.setVisibility(View.GONE);
                    labelOrganicType.setVisibility(View.GONE);
                    inputOrganicType.setVisibility(View.GONE);
                    inputOrganicType2.setVisibility(View.GONE);
                    labelStructure.setVisibility(View.GONE);
                    inputStructure.setVisibility(View.GONE);
                    inputStructureType.setVisibility(View.GONE);
                    inputStructureType2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        inputOrganicType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Domain domain = (Domain) inputOrganicType2.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (!domain.getCodigo().equals("0")) {
                        labelTextureOther.setVisibility(View.GONE);
                        inputTextureOther.setVisibility(View.GONE);
                    } else {
                        labelTextureOther.setVisibility(View.VISIBLE);
                        inputTextureOther.setVisibility(View.VISIBLE);
                    }
                } else {
                    labelTextureOther.setVisibility(View.GONE);
                    inputTextureOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        inputStructure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Domain domain = (Domain) inputStructure.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {

                    structureTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureType);
                    inputStructureType.setAdapter(structureTypeAdapter);

                    switch(domain.getCodigo()){
                        case "PR":
                        case "BO":
                            labelStructureForm.setVisibility(View.VISIBLE);
                            inputStructureForm.setVisibility(View.VISIBLE);
                            labelStructureReasons.setVisibility(View.GONE);
                            inputStructureReasons.setVisibility(View.GONE);
                            break;
                        case "SE":
                            labelStructureForm.setVisibility(View.GONE);
                            inputStructureForm.setVisibility(View.GONE);
                            labelStructureReasons.setVisibility(View.VISIBLE);
                            inputStructureReasons.setVisibility(View.VISIBLE);
                            break;
                        case "CU":
                            List<Domain> listStructureTypeTemp = new LinkedList<>();
                            for(Domain row:listStructureType){
                                if(row.getValor()==null) {
                                    listStructureTypeTemp.add(row);
                                } else if(Integer.valueOf(row.getValor()) >= 4 && Integer.valueOf(row.getValor()) <= 8) {
                                    listStructureTypeTemp.add(row);
                                }
                            }
                            structureTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureTypeTemp);
                            inputStructureType.setAdapter(structureTypeAdapter);

                            labelStructureForm.setVisibility(View.GONE);
                            inputStructureForm.setVisibility(View.GONE);
                            labelStructureReasons.setVisibility(View.GONE);
                            inputStructureReasons.setVisibility(View.GONE);
                            // TODO Auto-generated method stub
                            break;
                        default :
                            labelStructureForm.setVisibility(View.GONE);
                            inputStructureForm.setVisibility(View.GONE);
                            labelStructureReasons.setVisibility(View.GONE);
                            inputStructureReasons.setVisibility(View.GONE);
                    }
                } else {
                    labelStructureTypeOther.setVisibility(View.GONE);
                    inputStructureTypeOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        inputStructureType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Domain domain = (Domain) inputStructureType2.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (!domain.getCodigo().equals("0")) {
                        labelStructureTypeOther.setVisibility(View.GONE);
                        inputStructureTypeOther.setVisibility(View.GONE);
                    } else {
                        labelStructureTypeOther.setVisibility(View.VISIBLE);
                        inputStructureTypeOther.setVisibility(View.VISIBLE);
                    }
                } else {
                    labelStructureTypeOther.setVisibility(View.GONE);
                    inputStructureTypeOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        populateDomains();

        return rootView;
    }

    public void onClick(View v) {

    }

    void populateDomains() {
        DataBaseHelper dataBaseHelper =  new DataBaseHelper(getActivity());
        List<Domain> listReconocedor = dataBaseHelper.getListDomain("Y1 - RECONOCEDOR");
        List<Domain> listEpoca = dataBaseHelper.getListDomain("EPOCA");
        List<Domain> listLongitud = dataBaseHelper.getListDomain("X2 - LONGITUD");
        List<Domain> listErosion = dataBaseHelper.getListDomain("G1 - GRADO DE EROSION");
        List<Domain> listMovimiento = dataBaseHelper.getListDomain("G5 - TIPOS  DE MOVIMIENTOS EN MASA");
        List<Domain> listAnegamiento = dataBaseHelper.getListDomain("U2 - ANEGAMIENTO");
        List<Domain> listFrecuencia = dataBaseHelper.getListDomain("U3 - FRECUENCIA");
        List<Domain> listDuracion = dataBaseHelper.getListDomain("U4 - DURACION");
        List<Domain> listColorHue = dataBaseHelper.getListDomain("Z1 - MATRIZ HUE");
        List<Domain> listColorValue = dataBaseHelper.getListDomain("Z2 - MATRIZ VALUE");
        List<Domain> listColorChroma = dataBaseHelper.getListDomain("Z3 - MATRIZ CHROMA");
        List<Domain> listMaterialType = dataBaseHelper.getListDomain("H4 - TIPO DE MATERIAL");
        List<Domain> listTexture = dataBaseHelper.getListDomain("T1 - CLASES TEXTURALES DEL SUELO");
        List<Domain> listTextureModifiers = dataBaseHelper.getListDomain("T2 - MODIFICADORES DE LA TEXTURA");
        List<Domain> listOrganicType = dataBaseHelper.getListDomain("O1 - CLASES DE MATERIALES ORGÁNICOS");
        List<Domain> listOrganicType2 = dataBaseHelper.getListDomain("O2 - CLASES POR COMPOSICIÓN DE LOS MATERIALES ORGÁNICOS");
        List<Domain> listStructure = dataBaseHelper.getListDomain("E1 - TIPOS DE ESTRUCTURA DE SUELO");
        listStructureType = dataBaseHelper.getListDomain("E2 - CLASES DE ESTRUCTURA DE SUELO POR TAMAÑO");
        List<Domain> listStructureType2 = dataBaseHelper.getListDomain("E3 - CLASES POR GRADO DE LA ESTRUCTURA");
        List<Domain> listStructureForms = dataBaseHelper.getListDomain("E4 - FORMAS EN QUE ROMPE");
        List<Domain> listStructureReasons = dataBaseHelper.getListDomain("E5 - MOTIVO DE LA NO-ESTRUCTURA");
        List<Domain> listHorizontes = dataBaseHelper.getListDomain("H1 - HORIZONTES MAESTROS");
        List<Domain> listSoilFragment = dataBaseHelper.getListDomain("S3 - FRAGMENTOS EN SUELO");
        List<Domain> listNaturalDrainage = dataBaseHelper.getListDomain("U1 - DRENAJE NATURAL");
        List<Domain> listEffectiveDepth = dataBaseHelper.getListDomain("S1 - PROFUNDIDAD EFECTIVA");

        ArrayAdapter<Domain> reconocedorAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listReconocedor);
        inputReconocedor.setAdapter(reconocedorAdapter);

        ArrayAdapter<Domain> epocaAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listEpoca);
        inputEpoca.setAdapter(epocaAdapter);

        ArrayAdapter<Domain> longitudAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listLongitud);
        inputLongitud.setAdapter(longitudAdapter);

        ArrayAdapter<Domain> erosionAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listErosion);
        inputErosion.setAdapter(erosionAdapter);

        ArrayAdapter<Domain> movimientoAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listMovimiento);
        inputMovimiento.setAdapter(movimientoAdapter);

        ArrayAdapter<Domain> anegamientoAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listAnegamiento);
        inputAnegamiento.setAdapter(anegamientoAdapter);

        ArrayAdapter<Domain> frecuenciaAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listFrecuencia);
        inputFrecuencia.setAdapter(frecuenciaAdapter);

        ArrayAdapter<Domain> duracionAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listDuracion);
        inputDuracion.setAdapter(duracionAdapter);

        colorHueAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorHue);
        inputColorHue.setAdapter(colorHueAdapter);

        colorValueAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorValue);
        inputColorValue.setAdapter(colorValueAdapter);

        colorChromaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorChroma);
        inputColorChroma.setAdapter(colorChromaAdapter);

        materialTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listMaterialType);
        inputMaterialType.setAdapter(materialTypeAdapter);

        textureAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listTexture);
        inputTexture.setAdapter(textureAdapter);

        textureModifiersAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listTextureModifiers);
        inputTextureModifiers.setAdapter(textureModifiersAdapter);

        organicTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listOrganicType);
        inputOrganicType.setAdapter(organicTypeAdapter);

        organicTypeAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listOrganicType2);
        inputOrganicType2.setAdapter(organicTypeAdapter2);

        structureAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructure);
        inputStructure.setAdapter(structureAdapter);

        structureTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureType);
        inputStructureType.setAdapter(structureTypeAdapter);

        structureTypeAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureType2);
        inputStructureType2.setAdapter(structureTypeAdapter2);

        structureFormsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureForms);
        inputStructureForm.setAdapter(structureFormsAdapter);

        structureReasonsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureReasons);
        inputStructureReasons.setAdapter(structureReasonsAdapter);

        skylineTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listHorizontes);
        inputSkylineType.setAdapter(skylineTypeAdapter);

        ArrayAdapter<Domain> soilFragmentAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listSoilFragment);
        inputSoilFragment.setAdapter(soilFragmentAdapter);

        ArrayAdapter<Domain> naturalDrainageAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listNaturalDrainage);
        inputNaturalDrainage.setAdapter(naturalDrainageAdapter);

        ArrayAdapter<Domain> effectiveDepthAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listEffectiveDepth);
        inputEffectiveDepth.setAdapter(effectiveDepthAdapter);
    }

    public void previewAction(){
        if(mode == Modes.EDIT) {
            String depth = inputDepth.getText().toString();
            Domain colorHue = ((Domain)inputColorHue.getSelectedItem());
            Domain colorValue = ((Domain)inputColorValue.getSelectedItem());
            Domain colorChroma = ((Domain)inputColorChroma.getSelectedItem());
            String colorPercent = inputTexturePercent.getText().toString();
            Domain materialType = ((Domain) inputMaterialType.getSelectedItem());
            Domain texture = ((Domain) inputTexture.getSelectedItem());
            Domain textureModifiers = ((Domain) inputTextureModifiers.getSelectedItem());
            String texturePercent = inputTexturePercent.getText().toString();
            Domain structure = ((Domain) inputStructure.getSelectedItem());
            Domain skylineType = ((Domain) inputSkylineType.getSelectedItem());
            if(!depth.isEmpty() && colorHue.getId()!=null && colorValue.getId()!=null
                    && colorChroma.getId()!=null
                    && texture.getId()!=null) {
                setValues(depth, colorHue, colorValue, colorChroma, colorPercent,
                        materialType, texture, textureModifiers,
                        texturePercent, structure, skylineType);
                index--;
                if(index >= 0) {
                    getValues();
                    if(index == 0) {
                        btnPreview.setEnabled(false);
                    }
                }
            } else {
                Toast.makeText(getActivity(),
                        "Los campos marcados con * son obligatorios. Sin embargo por ahora es obligatorio Profundidad, Color" +
                                " y Textura", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            index--;
            if(index >= 0) {
                getValues();
                if(index == 0) {
                    btnPreview.setEnabled(false);
                }
            }
        }
    }

    public void nextAction(){
        String depth = inputDepth.getText().toString();
        Domain colorHue = ((Domain)inputColorHue.getSelectedItem());
        Domain colorValue = ((Domain)inputColorValue.getSelectedItem());
        Domain colorChroma = ((Domain)inputColorChroma.getSelectedItem());
        String colorPercent = inputTexturePercent.getText().toString();
        Domain materialType = ((Domain) inputMaterialType.getSelectedItem());
        Domain texture = ((Domain) inputTexture.getSelectedItem());
        Domain textureModifiers = ((Domain) inputTextureModifiers.getSelectedItem());
        String texturePercent = inputTexturePercent.getText().toString();
        Domain structure = ((Domain) inputStructure.getSelectedItem());
        Domain skylineType = ((Domain) inputSkylineType.getSelectedItem());

        if(!depth.isEmpty() && colorHue.getId()!=null && colorValue.getId()!=null
                && colorChroma.getId()!=null
                && texture.getId()!=null) {
            btnPreview.setEnabled(true);
            index++;
            if (mode == Modes.NEW) {
                skylineObject = new Skyline();
                setValues(depth, colorHue, colorValue, colorChroma, colorPercent, materialType,
                        texture, textureModifiers, texturePercent, structure, skylineType);
                skylineList.add(skylineObject);
            } else {
                setValues(depth, colorHue, colorValue, colorChroma, colorPercent, materialType,
                        texture, textureModifiers, texturePercent, structure, skylineType);
            }
            getValues();

            SkylineAdapter skylineAdapter = new SkylineAdapter(getActivity(), getFragmentManager(),
                    R.layout.list_skyline_item, skylineList);
            listSkyline.setItemsCanFocus(false);
            listSkyline.setAdapter(skylineAdapter);
        } else {
            Toast.makeText(getActivity(),
                    "Los campos marcados con * son obligatorios. Sin embargo por ahora es obligatorio Profundidad, Color" +
                            " y Textura", Toast.LENGTH_LONG)
                    .show();
        }
    }

    void clearFields() {
        inputDepth.setText(null);
        inputColorHue.setSelection(0);
        inputColorValue.setSelection(0);
        inputColorChroma.setSelection(0);
        inputColorPercent.setText(null);
        inputMaterialType.setSelection(0);
        inputTexture.setSelection(0);
        inputTexturePercent.setText(null);
        inputTextureModifiers.setSelection(0);
        inputStructure.setSelection(0);
        inputSkylineType.setSelection(0);
    }

    void getValues() {
        labelHorizonteNro.setText(String.format(getResources().getString(R.string.tst_horizonte_nro),(index + 1)));
        try{
            skylineObject = skylineList.get(index);
            if (skylineObject != null) {
                labelHorizonteNro.setText(String.format(getResources().getString(R.string.tst_horizonte_nro),(index + 1)));
                inputDepth.setText(skylineObject.getDepth().toString());
                inputColorPercent.setText(skylineObject.getColorPercent());
                inputTexturePercent.setText(skylineObject.getTexturePercent().toString());
                int spinnerPosition = colorHueAdapter.getPosition(skylineObject.getHueColor());
                inputColorHue.setSelection(spinnerPosition);
                spinnerPosition = colorValueAdapter.getPosition(skylineObject.getValueColor());
                inputColorValue.setSelection(spinnerPosition);
                spinnerPosition = colorChromaAdapter.getPosition(skylineObject.getChromaColor());
                inputColorChroma.setSelection(spinnerPosition);
                spinnerPosition = materialTypeAdapter.getPosition(skylineObject.getMaterialType());
                inputMaterialType.setSelection(spinnerPosition);
                spinnerPosition = textureAdapter.getPosition(skylineObject.getTexture());
                inputTexture.setSelection(spinnerPosition);
                spinnerPosition = textureAdapter.getPosition(skylineObject.getTextureModifiers());
                inputTextureModifiers.setSelection(spinnerPosition);
                spinnerPosition = structureAdapter.getPosition(skylineObject.getStructure());
                inputStructure.setSelection(spinnerPosition);
                spinnerPosition = skylineTypeAdapter.getPosition(skylineObject.getSkylineType());
                inputSkylineType.setSelection(spinnerPosition);
                mode = Modes.EDIT;
            } else {
                mode = Modes.NEW;
                clearFields();
            }
        } catch(IndexOutOfBoundsException ex) {
            mode = Modes.NEW;
            clearFields();
        }
    }

    void setValues(String depth, Domain colorHue , Domain colorValue, Domain colorChroma, String colorPercent,
                           Domain materialType, Domain texture, Domain textureModifiers,
                           String texturePercent, Domain structure, Domain skylineType) {
        skylineObject.setSkylineNumber(index);
        skylineObject.setDepth(Integer.valueOf(depth));
        skylineObject.setHueColor(colorHue);
        skylineObject.setValueColor(colorValue);
        skylineObject.setChromaColor(colorChroma);
        skylineObject.setColorPercent(Integer.valueOf(colorPercent));
        skylineObject.setMaterialType(materialType);
        skylineObject.setTexture(texture);
        skylineObject.setTextureModifiers(textureModifiers);
        skylineObject.setTexturePercent(Integer.valueOf(texturePercent));
        skylineObject.setStructure(structure);
        skylineObject.setSkylineType(skylineType);
    }

    public void removeItemSkyline() {
        if(skylineList.isEmpty()) {
            clearFields();
            btnPreview.setEnabled(false);
            index = 0;
            mode = Modes.NEW;
            labelHorizonteNro.setText(String.format(getResources().getString(R.string.tst_horizonte_nro),(index + 1)));
        } else {
            int i = 0;
            for(Skyline row:skylineList) {
                row.setSkylineNumber(i++);
            }
            index = skylineList.size() - 1;
            getValues();
        }
    }

    public void editItemSkyline(int position) {
        index = position;
        getValues();
        host.setCurrentTab(2);
    }

    public void addOptionalView(LayoutInflater inflater, SkylineType skylineType){
        View rootView = inflater.inflate(R.layout.tab_testing_skyline, null);

        TextView labelHorizonteNroOpt = (TextView) rootView.findViewById(R.id.label_horizonte_nro);
        if(skylineType.equals(SkylineType.OPTIONAL)) {
            rootView.setId(indexOptional++);
            labelHorizonteNroOpt.setText(String.format(getResources().getString(R.string.tst_skyline_optional), indexOptional));
        } else {
            rootView.setId(indexFlecked++);
            labelHorizonteNroOpt.setText(String.format(getResources().getString(R.string.tst_skyline_flecked), indexFlecked));
        }

        linearLayoutOpt.addView(rootView);
        viewList.add(rootView);

        final TextView labelHorizonteNro = (TextView) rootView.findViewById(R.id.label_horizonte_nro);
        final EditText inputDepth = (EditText) rootView.findViewById(R.id.input_profundidad);
        final Spinner inputColorHue = (Spinner) rootView.findViewById(R.id.input_color_hue);
        final Spinner inputColorValue = (Spinner) rootView.findViewById(R.id.input_color_value);
        final Spinner inputColorChroma = (Spinner) rootView.findViewById(R.id.input_color_chroma);
        final EditText inputColorPercent = (EditText) rootView.findViewById(R.id.input_color_percent);
        final Spinner inputMaterialType = (Spinner) rootView.findViewById(R.id.input_material_type);
        final TextView labelTexture = (TextView) rootView.findViewById(R.id.label_texture);
        final Spinner inputTexture = (Spinner) rootView.findViewById(R.id.input_texture);
        final TextView labelTextureOther = (TextView) rootView.findViewById(R.id.label_texture_other);
        final EditText inputTextureOther = (EditText) rootView.findViewById(R.id.input_texture_other);
        final Spinner inputTextureModifiers  = (Spinner) rootView.findViewById(R.id.input_texture_modifiers);
        final EditText inputTexturePercent = (EditText) rootView.findViewById(R.id.input_texture_percent);
        final TextView labelOrganicType = (TextView) rootView.findViewById(R.id.label_organic_type);
        final Spinner inputOrganicType  = (Spinner) rootView.findViewById(R.id.input_organic_type);
        final Spinner inputOrganicType2  = (Spinner) rootView.findViewById(R.id.input_organic_type2);
        final TextView labelStructure = (TextView) rootView.findViewById(R.id.label_structure);
        final Spinner inputStructure = (Spinner) rootView.findViewById(R.id.input_structure);
        final Spinner inputStructureType = (Spinner) rootView.findViewById(R.id.input_structure_type);
        final Spinner inputStructureType2 = (Spinner) rootView.findViewById(R.id.input_structure_type2);
        final TextView labelStructureTypeOther = (TextView) rootView.findViewById(R.id.label_structure_type_other);
        final EditText inputStructureTypeOther = (EditText) rootView.findViewById(R.id.input_structure_type_other);
        final Spinner inputSkylineType = (Spinner) rootView.findViewById(R.id.input_horizontes);
        final TextView labelStructureForm = (TextView) rootView.findViewById(R.id.label_structure_form);
        final Spinner inputStructureForm = (Spinner) rootView.findViewById(R.id.input_structure_form);
        final TextView labelStructureReasons = (TextView) rootView.findViewById(R.id.label_structure_reasons);
        final Spinner inputStructureReasons = (Spinner) rootView.findViewById(R.id.input_structure_reasons);

        labelTexture.setVisibility(View.GONE);
        inputTexture.setVisibility(View.GONE);
        inputTextureModifiers.setVisibility(View.GONE);
        labelOrganicType.setVisibility(View.GONE);
        inputOrganicType.setVisibility(View.GONE);
        inputOrganicType2.setVisibility(View.GONE);
        labelTextureOther.setVisibility(View.GONE);
        inputTextureOther.setVisibility(View.GONE);
        labelStructure.setVisibility(View.GONE);
        inputStructure.setVisibility(View.GONE);
        inputStructureType.setVisibility(View.GONE);
        inputStructureType2.setVisibility(View.GONE);
        labelStructureTypeOther.setVisibility(View.GONE);
        inputStructureTypeOther.setVisibility(View.GONE);
        labelStructureForm.setVisibility(View.GONE);
        inputStructureForm.setVisibility(View.GONE);
        labelStructureReasons.setVisibility(View.GONE);
        inputStructureReasons.setVisibility(View.GONE);

        DataBaseHelper dataBaseHelper =  new DataBaseHelper(getActivity());
        List<Domain> listReconocedor = dataBaseHelper.getListDomain("Y1 - RECONOCEDOR");
        List<Domain> listEpoca = dataBaseHelper.getListDomain("EPOCA");
        List<Domain> listLongitud = dataBaseHelper.getListDomain("X2 - LONGITUD");
        List<Domain> listErosion = dataBaseHelper.getListDomain("G1 - GRADO DE EROSION");
        List<Domain> listMovimiento = dataBaseHelper.getListDomain("G5 - TIPOS  DE MOVIMIENTOS EN MASA");
        List<Domain> listAnegamiento = dataBaseHelper.getListDomain("U2 - ANEGAMIENTO");
        List<Domain> listFrecuencia = dataBaseHelper.getListDomain("U3 - FRECUENCIA");
        List<Domain> listDuracion = dataBaseHelper.getListDomain("U4 - DURACION");
        List<Domain> listColorHue = dataBaseHelper.getListDomain("Z1 - MATRIZ HUE");
        List<Domain> listColorValue = dataBaseHelper.getListDomain("Z2 - MATRIZ VALUE");
        List<Domain> listColorChroma = dataBaseHelper.getListDomain("Z3 - MATRIZ CHROMA");
        List<Domain> listMaterialType = dataBaseHelper.getListDomain("H4 - TIPO DE MATERIAL");
        List<Domain> listTexture = dataBaseHelper.getListDomain("T1 - CLASES TEXTURALES DEL SUELO");
        List<Domain> listTextureModifiers = dataBaseHelper.getListDomain("T2 - MODIFICADORES DE LA TEXTURA");
        List<Domain> listOrganicType = dataBaseHelper.getListDomain("O1 - CLASES DE MATERIALES ORGÁNICOS");
        List<Domain> listOrganicType2 = dataBaseHelper.getListDomain("O2 - CLASES POR COMPOSICIÓN DE LOS MATERIALES ORGÁNICOS");
        List<Domain> listStructure = dataBaseHelper.getListDomain("E1 - TIPOS DE ESTRUCTURA DE SUELO");
        listStructureType = dataBaseHelper.getListDomain("E2 - CLASES DE ESTRUCTURA DE SUELO POR TAMAÑO");
        List<Domain> listStructureType2 = dataBaseHelper.getListDomain("E3 - CLASES POR GRADO DE LA ESTRUCTURA");
        List<Domain> listStructureForms = dataBaseHelper.getListDomain("E4 - FORMAS EN QUE ROMPE");
        List<Domain> listStructureReasons = dataBaseHelper.getListDomain("E5 - MOTIVO DE LA NO-ESTRUCTURA");
        List<Domain> listHorizontes = dataBaseHelper.getListDomain("H1 - HORIZONTES MAESTROS");

        ArrayAdapter<Domain> reconocedorAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listReconocedor);
        inputReconocedor.setAdapter(reconocedorAdapter);

        ArrayAdapter<Domain> epocaAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listEpoca);
        inputEpoca.setAdapter(epocaAdapter);

        ArrayAdapter<Domain> longitudAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listLongitud);
        inputLongitud.setAdapter(longitudAdapter);

        ArrayAdapter<Domain> erosionAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listErosion);
        inputErosion.setAdapter(erosionAdapter);

        ArrayAdapter<Domain> movimientoAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listMovimiento);
        inputMovimiento.setAdapter(movimientoAdapter);

        ArrayAdapter<Domain> anegamientoAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listAnegamiento);
        inputAnegamiento.setAdapter(anegamientoAdapter);

        ArrayAdapter<Domain> frecuenciaAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listFrecuencia);
        inputFrecuencia.setAdapter(frecuenciaAdapter);

        ArrayAdapter<Domain> duracionAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listDuracion);
        inputDuracion.setAdapter(duracionAdapter);

        colorHueAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorHue);
        inputColorHue.setAdapter(colorHueAdapter);

        colorValueAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorValue);
        inputColorValue.setAdapter(colorValueAdapter);

        colorChromaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorChroma);
        inputColorChroma.setAdapter(colorChromaAdapter);

        materialTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listMaterialType);
        inputMaterialType.setAdapter(materialTypeAdapter);

        textureAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listTexture);
        inputTexture.setAdapter(textureAdapter);

        textureModifiersAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listTextureModifiers);
        inputTextureModifiers.setAdapter(textureModifiersAdapter);

        organicTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listOrganicType);
        inputOrganicType.setAdapter(organicTypeAdapter);

        organicTypeAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listOrganicType2);
        inputOrganicType2.setAdapter(organicTypeAdapter2);

        structureAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructure);
        inputStructure.setAdapter(structureAdapter);

        structureTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureType);
        inputStructureType.setAdapter(structureTypeAdapter);

        structureTypeAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureType2);
        inputStructureType2.setAdapter(structureTypeAdapter2);

        structureFormsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureForms);
        inputStructureForm.setAdapter(structureFormsAdapter);

        structureReasonsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureReasons);
        inputStructureReasons.setAdapter(structureReasonsAdapter);

        skylineTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listHorizontes);
        inputSkylineType.setAdapter(skylineTypeAdapter);

        inputMaterialType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Domain domain = (Domain) inputMaterialType.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (domain.getCodigo().equals("2")) {
                        labelTexture.setVisibility(View.GONE);
                        inputTexture.setVisibility(View.GONE);
                        inputTextureModifiers.setVisibility(View.GONE);
                        labelOrganicType.setVisibility(View.VISIBLE);
                        inputOrganicType.setVisibility(View.VISIBLE);
                        inputOrganicType2.setVisibility(View.VISIBLE);
                        labelStructure.setVisibility(View.GONE);
                        inputStructure.setVisibility(View.GONE);
                        inputStructureType.setVisibility(View.GONE);
                        inputStructureType2.setVisibility(View.GONE);
                    } else {
                        labelTexture.setVisibility(View.VISIBLE);
                        inputTexture.setVisibility(View.VISIBLE);
                        inputTextureModifiers.setVisibility(View.VISIBLE);
                        labelOrganicType.setVisibility(View.GONE);
                        inputOrganicType.setVisibility(View.GONE);
                        inputOrganicType2.setVisibility(View.GONE);
                        labelStructure.setVisibility(View.VISIBLE);
                        inputStructure.setVisibility(View.VISIBLE);
                        inputStructureType.setVisibility(View.VISIBLE);
                        inputStructureType2.setVisibility(View.VISIBLE);
                    }
                } else {
                    labelTexture.setVisibility(View.GONE);
                    inputTexture.setVisibility(View.GONE);
                    inputTextureModifiers.setVisibility(View.GONE);
                    labelOrganicType.setVisibility(View.GONE);
                    inputOrganicType.setVisibility(View.GONE);
                    inputOrganicType2.setVisibility(View.GONE);
                    labelStructure.setVisibility(View.GONE);
                    inputStructure.setVisibility(View.GONE);
                    inputStructureType.setVisibility(View.GONE);
                    inputStructureType2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        inputOrganicType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Domain domain = (Domain) inputOrganicType2.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (!domain.getCodigo().equals("0")) {
                        labelTextureOther.setVisibility(View.GONE);
                        inputTextureOther.setVisibility(View.GONE);
                    } else {
                        labelTextureOther.setVisibility(View.VISIBLE);
                        inputTextureOther.setVisibility(View.VISIBLE);
                    }
                } else {
                    labelTextureOther.setVisibility(View.GONE);
                    inputTextureOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        inputStructure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Domain domain = (Domain) inputStructure.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {

                    structureTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureType);
                    inputStructureType.setAdapter(structureTypeAdapter);

                    switch(domain.getCodigo()){
                        case "PR":
                        case "BO":
                            labelStructureForm.setVisibility(View.VISIBLE);
                            inputStructureForm.setVisibility(View.VISIBLE);
                            labelStructureReasons.setVisibility(View.GONE);
                            inputStructureReasons.setVisibility(View.GONE);
                            break;
                        case "SE":
                            labelStructureForm.setVisibility(View.GONE);
                            inputStructureForm.setVisibility(View.GONE);
                            labelStructureReasons.setVisibility(View.VISIBLE);
                            inputStructureReasons.setVisibility(View.VISIBLE);
                            break;
                        case "CU":
                            List<Domain> listStructureTypeTemp = new LinkedList<>();
                            for(Domain row:listStructureType){
                                if(row.getValor()==null) {
                                    listStructureTypeTemp.add(row);
                                } else if(Integer.valueOf(row.getValor()) >= 4 && Integer.valueOf(row.getValor()) <= 8) {
                                    listStructureTypeTemp.add(row);
                                }
                            }
                            structureTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureTypeTemp);
                            inputStructureType.setAdapter(structureTypeAdapter);

                            labelStructureForm.setVisibility(View.GONE);
                            inputStructureForm.setVisibility(View.GONE);
                            labelStructureReasons.setVisibility(View.GONE);
                            inputStructureReasons.setVisibility(View.GONE);
                            // TODO Auto-generated method stub
                            break;
                        default :
                            labelStructureForm.setVisibility(View.GONE);
                            inputStructureForm.setVisibility(View.GONE);
                            labelStructureReasons.setVisibility(View.GONE);
                            inputStructureReasons.setVisibility(View.GONE);
                    }
                } else {
                    labelStructureTypeOther.setVisibility(View.GONE);
                    inputStructureTypeOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        inputStructureType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Domain domain = (Domain) inputStructureType2.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (!domain.getCodigo().equals("0")) {
                        labelStructureTypeOther.setVisibility(View.GONE);
                        inputStructureTypeOther.setVisibility(View.GONE);
                    } else {
                        labelStructureTypeOther.setVisibility(View.VISIBLE);
                        inputStructureTypeOther.setVisibility(View.VISIBLE);
                    }
                } else {
                    labelStructureTypeOther.setVisibility(View.GONE);
                    inputStructureTypeOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void takePictureAction(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap pic = (Bitmap) extras.get("data");
        imgViewPic.setImageBitmap(pic);
    }

    public void loadPictureAction(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn,
                null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Bitmap pic = BitmapFactory.decodeFile(picturePath);
        imgViewPic.setImageBitmap(pic);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            loadPictureAction(data);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE
                && resultCode == RESULT_OK) {
            takePictureAction(data);
        }
    }

    public void onEvent(ActivityResultEvent event) {
        Log.d("haint", "Message from MainActivity via EvenBus: request code = " + event.getRequestCode());
    }

}
