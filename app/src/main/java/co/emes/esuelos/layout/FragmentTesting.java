package co.emes.esuelos.layout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.NestedScrollView;
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
import co.emes.esuelos.forms.FormComprobacionHorizonteAdapter;
import co.emes.esuelos.model.Domain;
import co.emes.esuelos.model.FormComprobacion;
import co.emes.esuelos.model.FormComprobacionFoto;
import co.emes.esuelos.model.FormComprobacionHorizonte;
import co.emes.esuelos.model.FormComprobacionHorizonteOpt;
import co.emes.esuelos.model.OptionalEntity;
import co.emes.esuelos.util.DBBitmapUtility;
import co.emes.esuelos.util.DataBaseHelper;
import co.emes.esuelos.util.Singleton;
import co.emes.esuelos.util.Utils;

/**
 * Created by csarmiento on 11/14/16
 */
public class FragmentTesting extends DialogFragment implements View.OnClickListener {

    public static final String TAG = FragmentTesting.class.getSimpleName();

    LayoutInflater inflater;
    TabHost host;
    EditText inputNroObservacion;
    EditText inputFecha;
    Spinner inputReconocedor;
    EditText inputNombreSitio;
    Spinner inputEpoca;
    EditText inputEpocaDias;
    Spinner inputLongitud;
    Spinner inputErosion;
    Spinner inputMovimiento;
    Spinner inputAnegamiento;
    TextView labelFrecuencia;
    Spinner inputFrecuencia;
    TextView labelDuracion;
    Spinner inputDuracion;
    Spinner inputPedegrosidad;
    Spinner inputAfloramiento;
    Spinner inputFragmentoSuelo;
    Spinner inputDrenajeNatural;
    Spinner inputProfundidadEfectiva;
    Spinner inputEpidedones;
    TextView labelEndopedones;
    Spinner inputEndopedones;
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
    Spinner inputHorizonteCaracterisitica;
    LinearLayout linearLayoutOpt;

    Button btnPreviewTop;
    Button btnPreview;

    List<Domain> listReconocedor;
    List<Domain> listEpoca;
    List<Domain> listLongitud;
    List<Domain> listErosion;
    List<Domain> listMovimiento;
    List<Domain> listAnegamiento;
    List<Domain> listFrecuencia;
    List<Domain> listDuracion;
    List<Domain> listPedegrosidad;
    List<Domain> listAfloramiento;
    List<Domain> listColorHue;
    List<Domain> listColorValue;
    List<Domain> listColorChroma;
    List<Domain> listMaterialType;
    List<Domain> listTexture;
    List<Domain> listTextureModifiers;
    List<Domain> listOrganicType;
    List<Domain> listOrganicType2;
    List<Domain> listStructure;
    List<Domain> listStructureType;
    List<Domain> listStructureType2;
    List<Domain> listStructureForms;
    List<Domain> listStructureReasons;
    List<Domain> listHorizonteClase;
    List<Domain> listHorizonteCaracterisitica;
    List<Domain> listSoilFragment;
    List<Domain> listNaturalDrainage;
    List<Domain> listEffectiveDepth;
    List<Domain> listEpidedones;
    List<Domain> listEndopedones;

    ArrayAdapter<Domain> reconocedorAdapter;
    ArrayAdapter<Domain> epocaAdapter;
    ArrayAdapter<Domain> longitudAdapter;
    ArrayAdapter<Domain> erosionAdapter;
    ArrayAdapter<Domain> movimientoAdapter;
    ArrayAdapter<Domain> anegamientoAdapter;
    ArrayAdapter<Domain> frecuenciaAdapter;
    ArrayAdapter<Domain> duracionAdapter;
    ArrayAdapter<Domain> pedegrosidadAdapter;
    ArrayAdapter<Domain> afloramientoAdapter;
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
    ArrayAdapter<Domain> horizontesMaestrosAdapter;
    ArrayAdapter<Domain> horizonteCaracterisiticaAdapter;

    ArrayAdapter<Domain> soilFragmentAdapter;
    ArrayAdapter<Domain> naturalDrainageAdapter;
    ArrayAdapter<Domain> effectiveDepthAdapter;
    ArrayAdapter<Domain> epidedonesAdapter;
    ArrayAdapter<Domain> endopedonesAdapter;

    NestedScrollView scrollviewProfile;
    FormComprobacionHorizonte formComprobacionHorizonte;
    List<FormComprobacionHorizonte> formComprobacionHorizonteList;
    ListView listViewSkyline;
    int index;
    int indexOptional;
    int indexFlecked;
    List<View> viewListOptional;
    List<View> viewListFlecked;
    List<OptionalEntity> listOptionalEntity;

    private static final int RESULT_OK = -1;
    public static final int RESULT_LOAD_IMAGE = 1000;
    public static final int REQUEST_IMAGE_CAPTURE = 1001;

    DataBaseHelper dataBaseHelper;
    FormComprobacion formComprobacion;
    FormComprobacionFoto formComprobacionFoto;

    boolean nextFlag = true;
    boolean nextFlagMaterialType = true;
    boolean nextFlagEstructuraTipo = true;
    static Modes mode;

    enum Modes {
        NEW, EDIT
    }

    enum SkylineType {
        OPTIONAL, FLECKED
    }

    public FragmentTesting() {
        super();
        index = 0;
        indexOptional = 0;
        indexFlecked = 0;
        viewListOptional = new LinkedList<>();
        viewListFlecked = new LinkedList<>();
        listOptionalEntity = new LinkedList<>();
        dataBaseHelper =  new DataBaseHelper(getActivity());
    }

    public static FragmentTesting newInstance(FormComprobacion formComprobacion) {
        FragmentTesting f = new FragmentTesting();
        f.setFormComprobacion(formComprobacion);
        mode = Modes.EDIT;
        return f;
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
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_testing, container, false);
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
        inputFecha = (EditText) rootView.findViewById(R.id.input_fecha);
        inputReconocedor = (Spinner) rootView.findViewById(R.id.input_reconocedor);
        inputNombreSitio = (EditText) rootView.findViewById(R.id.input_nombre_sitio);
        inputEpoca = (Spinner) rootView.findViewById(R.id.input_epoca);
        inputEpocaDias = (EditText) rootView.findViewById(R.id.input_epoca_dias);
        inputLongitud = (Spinner) rootView.findViewById(R.id.input_longitud);
        inputErosion = (Spinner) rootView.findViewById(R.id.input_erosion);
        inputMovimiento = (Spinner) rootView.findViewById(R.id.input_movimiento);
        inputAnegamiento = (Spinner) rootView.findViewById(R.id.input_anegamiento);
        labelFrecuencia = (TextView) rootView.findViewById(R.id.label_frecuencia);
        inputFrecuencia = (Spinner) rootView.findViewById(R.id.input_frecuencia);
        labelDuracion = (TextView) rootView.findViewById(R.id.label_duracion);
        inputDuracion = (Spinner) rootView.findViewById(R.id.input_duracion);
        inputPedegrosidad = (Spinner) rootView.findViewById(R.id.input_pedegrosidad);
        inputAfloramiento = (Spinner) rootView.findViewById(R.id.input_afloramiento);

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
        inputHorizonteCaracterisitica  = (Spinner) rootView.findViewById(R.id.input_horizonte_caracterisitica);
        labelStructureForm = (TextView) rootView.findViewById(R.id.label_structure_form);
        inputStructureForm = (Spinner) rootView.findViewById(R.id.input_structure_form);
        labelStructureReasons = (TextView) rootView.findViewById(R.id.label_structure_reasons);
        inputStructureReasons = (Spinner) rootView.findViewById(R.id.input_structure_reasons);

        inputFragmentoSuelo = (Spinner) rootView.findViewById(R.id.input_soil_fragment);
        inputDrenajeNatural = (Spinner) rootView.findViewById(R.id.input_natural_drainage);
        inputProfundidadEfectiva = (Spinner) rootView.findViewById(R.id.input_effective_depth);
        inputEpidedones = (Spinner) rootView.findViewById(R.id.input_epidedones);
        labelEndopedones = (TextView) rootView.findViewById(R.id.label_endopedones);
        inputEndopedones = (Spinner) rootView.findViewById(R.id.input_endopedones);

        scrollviewProfile = (NestedScrollView) rootView.findViewById(R.id.scrollview_profile);
        listViewSkyline = (ListView) rootView.findViewById(R.id.list_skyline);

        Button btnSave = (Button) rootView.findViewById(R.id.btn_save);
        Button btnTakePhoto = (Button) rootView.findViewById(R.id.btn_take_photo);
        Button btnSelectPhoto = (Button) rootView.findViewById(R.id.btn_select_photo);

        btnSave.setTransformationMethod(null);
        btnTakePhoto.setTransformationMethod(null);
        btnSelectPhoto.setTransformationMethod(null);

        Button btnOptionalTop = (Button) rootView.findViewById(R.id.btn_optional_top);
        Button btnFleckedTop = (Button) rootView.findViewById(R.id.btn_add_flecked_top);
        Button btnNextTop = (Button) rootView.findViewById(R.id.btn_next_top);
        btnPreviewTop = (Button) rootView.findViewById(R.id.btn_preview_top);

        Button btnOptional = (Button) rootView.findViewById(R.id.btn_optional);
        Button btnFlecked = (Button) rootView.findViewById(R.id.btn_add_flecked);
        Button btnNext = (Button) rootView.findViewById(R.id.btn_next);
        btnPreview = (Button) rootView.findViewById(R.id.btn_preview);

        btnOptionalTop.setTransformationMethod(null);
        btnFleckedTop.setTransformationMethod(null);
        btnNextTop.setTransformationMethod(null);
        btnPreviewTop.setTransformationMethod(null);
        btnPreviewTop.setEnabled(false);

        btnOptional.setTransformationMethod(null);
        btnFlecked.setTransformationMethod(null);
        btnNext.setTransformationMethod(null);
        btnPreview.setTransformationMethod(null);
        btnPreview.setEnabled(false);

        linearLayoutOpt = (LinearLayout) rootView.findViewById(R.id.lyr_optional);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                nextFlag = false;
                nextFlagMaterialType = false;
                nextFlagEstructuraTipo = false;
            }
        });

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAction();
            }
        });

        btnNextTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAction();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAction();
            }
        });

        btnPreviewTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewAction();
            }
        });

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewAction();
            }
        });

        btnOptionalTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOptionalView(SkylineType.OPTIONAL);
                bottomScrollView();
            }
        });

        btnOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOptionalView(SkylineType.OPTIONAL);
                bottomScrollView();
            }
        });

        btnFleckedTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOptionalView(SkylineType.FLECKED);
                bottomScrollView();
            }
        });

        btnFlecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOptionalView(SkylineType.FLECKED);
                bottomScrollView();
            }
        });

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

        formComprobacionHorizonteList = new LinkedList<>();
        formComprobacionHorizonte =  new FormComprobacionHorizonte();

        inputAnegamiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                int positionDrenajeNatural = 0;
                if(nextFlag) {
                    clearAnegamiento();
                } else {
                    positionDrenajeNatural = inputDrenajeNatural.getSelectedItemPosition();
                }

                Domain domain = (Domain) inputAnegamiento.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (domain.getCodigo().equals("3")) {
                        labelFrecuencia.setVisibility(View.GONE);
                        labelDuracion.setVisibility(View.GONE);
                        inputFrecuencia.setVisibility(View.GONE);
                        inputDuracion.setVisibility(View.GONE);

                        List<Domain> listNaturalDrainageTemp = new LinkedList<>();
                        for(Domain row:listNaturalDrainage){
                            if(row.getValor()==null) {
                                listNaturalDrainageTemp.add(row);
                            } else if(Integer.valueOf(row.getValor()) >= 1 && Integer.valueOf(row.getValor()) <= 5) {
                                listNaturalDrainageTemp.add(row);
                            }
                        }
                        naturalDrainageAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listNaturalDrainageTemp);
                        inputDrenajeNatural.setAdapter(naturalDrainageAdapter);
                        inputDrenajeNatural.setSelection(positionDrenajeNatural);
                    } else {
                        labelFrecuencia.setVisibility(View.VISIBLE);
                        labelDuracion.setVisibility(View.VISIBLE);
                        inputFrecuencia.setVisibility(View.VISIBLE);
                        inputDuracion.setVisibility(View.VISIBLE);
                        naturalDrainageAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listNaturalDrainage);
                        inputDrenajeNatural.setAdapter(naturalDrainageAdapter);
                        inputDrenajeNatural.setSelection(positionDrenajeNatural);
                    }
                } else {
                    labelFrecuencia.setVisibility(View.GONE);
                    labelDuracion.setVisibility(View.GONE);
                    inputFrecuencia.setVisibility(View.GONE);
                    inputDuracion.setVisibility(View.GONE);
                    naturalDrainageAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listNaturalDrainage);
                    inputDrenajeNatural.setAdapter(naturalDrainageAdapter);
                    inputDrenajeNatural.setSelection(positionDrenajeNatural);
                }

                nextFlag = true;
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
                if(nextFlagMaterialType) {
                    clearStructureFields();
                    clearOrganicFields();
                }

                nextFlagMaterialType = true;

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
                        labelTextureOther.setVisibility(View.GONE);
                        inputTextureOther.setVisibility(View.GONE);
                        labelStructureTypeOther.setVisibility(View.GONE);
                        inputStructureTypeOther.setVisibility(View.GONE);
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
                        labelTextureOther.setVisibility(View.GONE);
                        inputTextureOther.setVisibility(View.GONE);
                        labelStructureTypeOther.setVisibility(View.GONE);
                        inputStructureTypeOther.setVisibility(View.GONE);
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
                    labelTextureOther.setVisibility(View.GONE);
                    inputTextureOther.setVisibility(View.GONE);
                    labelStructureTypeOther.setVisibility(View.GONE);
                    inputStructureTypeOther.setVisibility(View.GONE);
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

                int positionStructureType = 0;
                if(!nextFlagEstructuraTipo) {
                    positionStructureType = inputStructureType.getSelectedItemPosition();
                }

                Domain domain = (Domain) inputStructure.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {

                    structureTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listStructureType);
                    inputStructureType.setAdapter(structureTypeAdapter);
                    inputStructureType.setSelection(positionStructureType);

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
                            inputStructureType.setSelection(positionStructureType);

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

                nextFlagEstructuraTipo = true;
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

        if(mode == null || mode == Modes.NEW) {
            inputNroObservacion.setText(dataBaseHelper.getNroObservacion("C"));
            inputFecha.setText(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            labelHorizonteNro.setText(String.format(getResources().getString(R.string.tst_horizonte_nro), (index + 1)));
            mode = Modes.NEW;
        } else {
            editForm();
        }

        return rootView;
    }

    public void onClick(View v) {

    }

    public FormComprobacion getFormComprobacion() {
        return formComprobacion;
    }

    public void setFormComprobacion(FormComprobacion formComprobacion) {
        this.formComprobacion = formComprobacion;
    }

    void populateDomains() {
        listReconocedor = dataBaseHelper.getListDomain("Y1 - RECONOCEDOR");
        listEpoca = dataBaseHelper.getListDomain("EPOCA");
        listLongitud = dataBaseHelper.getListDomain("X2 - LONGITUD");
        listErosion = dataBaseHelper.getListDomain("G1 - GRADO DE EROSION");
        listMovimiento = dataBaseHelper.getListDomain("G5 - TIPOS  DE MOVIMIENTOS EN MASA");
        listAnegamiento = dataBaseHelper.getListDomain("U2 - ANEGAMIENTO");
        listFrecuencia = dataBaseHelper.getListDomain("U3 - FRECUENCIA");
        listDuracion = dataBaseHelper.getListDomain("U4 - DURACION");
        listPedegrosidad = dataBaseHelper.getListDomain("S4 - PEDREGOSIDAD SUPERFICIAL");
        listAfloramiento = dataBaseHelper.getListDomain("S5 - AFLORAMIENTO ROCOSO");
        listColorHue = dataBaseHelper.getListDomain("Z1 - MATRIZ HUE");
        listColorValue = dataBaseHelper.getListDomain("Z2 - MATRIZ VALUE");
        listColorChroma = dataBaseHelper.getListDomain("Z3 - MATRIZ CHROMA");
        listMaterialType = dataBaseHelper.getListDomain("H4 - TIPO DE MATERIAL");
        listTexture = dataBaseHelper.getListDomain("T1 - CLASES TEXTURALES DEL SUELO");
        listTextureModifiers = dataBaseHelper.getListDomain("T2 - MODIFICADORES DE LA TEXTURA");
        listOrganicType = dataBaseHelper.getListDomain("O1 - CLASES DE MATERIALES ORGÁNICOS");
        listOrganicType2 = dataBaseHelper.getListDomain("O2 - CLASES POR COMPOSICIÓN DE LOS MATERIALES ORGÁNICOS");
        listStructure = dataBaseHelper.getListDomain("E1 - TIPOS DE ESTRUCTURA DE SUELO");
        listStructureType = dataBaseHelper.getListDomain("E2 - CLASES DE ESTRUCTURA DE SUELO POR TAMAÑO");
        listStructureType2 = dataBaseHelper.getListDomain("E3 - CLASES POR GRADO DE LA ESTRUCTURA");
        listStructureForms = dataBaseHelper.getListDomain("E4 - FORMAS EN QUE ROMPE");
        listStructureReasons = dataBaseHelper.getListDomain("E5 - MOTIVO DE LA NO-ESTRUCTURA");
        listHorizonteClase = dataBaseHelper.getListDomainShowCode("H1 - HORIZONTES MAESTROS");
        listHorizonteCaracterisitica = dataBaseHelper.getListDomainShowCode("H2 - CLASES POR CARACTERÍSTICAS SUBORDINADAS A LOS HORIZONTES");
        listSoilFragment = dataBaseHelper.getListDomain("S3 - FRAGMENTOS EN SUELO");
        listNaturalDrainage = dataBaseHelper.getListDomain("U1 - DRENAJE NATURAL");
        listEffectiveDepth = dataBaseHelper.getListDomain("S1 - PROFUNDIDAD EFECTIVA");
        listEpidedones = dataBaseHelper.getListDomain("B1 - EPIPEDONES");
        listEndopedones = dataBaseHelper.getListDomain("B2 - ENDOPEDONES");

        reconocedorAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listReconocedor);
        inputReconocedor.setAdapter(reconocedorAdapter);

        epocaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listEpoca);
        inputEpoca.setAdapter(epocaAdapter);

        longitudAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listLongitud);
        inputLongitud.setAdapter(longitudAdapter);

        erosionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listErosion);
        inputErosion.setAdapter(erosionAdapter);

        movimientoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listMovimiento);
        inputMovimiento.setAdapter(movimientoAdapter);

        anegamientoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listAnegamiento);
        inputAnegamiento.setAdapter(anegamientoAdapter);

        frecuenciaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listFrecuencia);
        inputFrecuencia.setAdapter(frecuenciaAdapter);

        duracionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listDuracion);
        inputDuracion.setAdapter(duracionAdapter);

        pedegrosidadAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listPedegrosidad);
        inputPedegrosidad.setAdapter(pedegrosidadAdapter);

        afloramientoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listAfloramiento);
        inputAfloramiento.setAdapter(afloramientoAdapter);

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

        horizontesMaestrosAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listHorizonteClase);
        inputSkylineType.setAdapter(horizontesMaestrosAdapter);

        horizonteCaracterisiticaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listHorizonteCaracterisitica);
        inputHorizonteCaracterisitica.setAdapter(horizonteCaracterisiticaAdapter);

        soilFragmentAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listSoilFragment);
        inputFragmentoSuelo.setAdapter(soilFragmentAdapter);

        naturalDrainageAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listNaturalDrainage);
        inputDrenajeNatural.setAdapter(naturalDrainageAdapter);

        effectiveDepthAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listEffectiveDepth);
        inputProfundidadEfectiva.setAdapter(effectiveDepthAdapter);

        epidedonesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listEpidedones);
        inputEpidedones.setAdapter(epidedonesAdapter);

        endopedonesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listEndopedones);
        inputEndopedones.setAdapter(endopedonesAdapter);
    }

    public void previewAction(){
        if(mode == Modes.EDIT) {
            String depth = inputDepth.getText().toString();
            Domain colorHue = ((Domain)inputColorHue.getSelectedItem());
            Domain colorValue = ((Domain)inputColorValue.getSelectedItem());
            Domain colorChroma = ((Domain)inputColorChroma.getSelectedItem());
            String colorPercent = inputColorPercent.getText().toString();
            Domain materialType = ((Domain) inputMaterialType.getSelectedItem());
            Domain texture = new Domain();
            if(materialType.getCodigo()!=null) {
                if (materialType.getCodigo().equals("1")) {
                    texture = ((Domain) inputTexture.getSelectedItem());
                } else {
                    texture = ((Domain) inputOrganicType.getSelectedItem());
                }
            }

            Domain texturaClase = ((Domain) inputTexture.getSelectedItem());
            Domain textureModifiers = ((Domain) inputTextureModifiers.getSelectedItem());
            Domain organicoClase = ((Domain) inputOrganicType.getSelectedItem());
            Domain organicoComposicion = ((Domain) inputOrganicType2.getSelectedItem());
            String texturePercent = inputTexturePercent.getText().toString();
            Domain estructuraTipo = ((Domain) inputStructure.getSelectedItem());
            Domain estructuraClase = ((Domain) inputStructureType.getSelectedItem());
            Domain estructuraGrado = ((Domain) inputStructureType2.getSelectedItem());
            Domain formaRompe = ((Domain) inputStructureForm.getSelectedItem());
            Domain motivoNoEstructura = ((Domain) inputStructureReasons.getSelectedItem());
            String estructuraOtra = inputStructureTypeOther.getText().toString();
            Domain skylineType = ((Domain) inputSkylineType.getSelectedItem());
            Domain horizonteCaracterisitica = ((Domain) inputHorizonteCaracterisitica.getSelectedItem());

            if(!depth.isEmpty() && colorHue.getId()!=null && colorValue.getId()!=null
                    && colorChroma.getId()!=null
                    && texture.getId()!=null) {
                setValues(depth, colorHue, colorValue, colorChroma, colorPercent,
                        materialType, texturaClase, textureModifiers, texturePercent,
                        organicoClase, organicoComposicion,
                        estructuraTipo, estructuraClase, estructuraGrado, formaRompe, motivoNoEstructura,
                        estructuraOtra, skylineType, horizonteCaracterisitica);
                index--;
                if(index >= 0) {
                    getValues();
                    if(index == 0) {
                        btnPreview.setEnabled(false);
                        btnPreviewTop.setEnabled(false);
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
                    btnPreviewTop.setEnabled(false);
                }
            }
        }
    }

    public List<FormComprobacionHorizonteOpt> validateOptional(Integer idFormComprobacion){
        List<FormComprobacionHorizonteOpt> listFormComprobacionHorizonteOpt = new LinkedList<>();
        for(View row:viewListOptional){
            FormComprobacionHorizonteOpt entity = new FormComprobacionHorizonteOpt();
            entity.setIdFormComprobacionHorz(idFormComprobacion);
            LinearLayout child = (LinearLayout)row;
            for(int index = 0; index < child.getChildCount(); index++) {
                View object = child.getChildAt(index);
                if (object instanceof LinearLayout && object.getId() == R.id.lyr_colors) {
                    LinearLayout lyr = (LinearLayout)object;
                    for(int index2 = 0; index2 < lyr.getChildCount(); index2++) {
                        View object2 = lyr.getChildAt(index2);
                        if (object2 instanceof Spinner) {
                            Spinner spinner = (Spinner) object2;
                            switch (spinner.getId()) {
                                case R.id.input_color_hue:
                                    entity.setColorHue(((Domain) spinner.getSelectedItem()).getId());
                                    break;
                                case R.id.input_color_value:
                                    entity.setColorValue(((Domain) spinner.getSelectedItem()).getId());
                                    break;
                                case R.id.input_color_chroma:
                                    entity.setColorChroma(((Domain) spinner.getSelectedItem()).getId());
                                    break;
                            }
                        }
                    }
                } else if (object instanceof EditText) {
                    EditText editText = (EditText) object;
                    if (editText.getId() == R.id.input_color_percent) {
                        try {
                            entity.setColorPorcentaje(Integer.valueOf(editText.getText().toString()));
                        } catch (Exception ex) {
                            entity.setColorPorcentaje(null);
                        }
                    }
                }
            }
            listFormComprobacionHorizonteOpt.add(entity);
        }
        return listFormComprobacionHorizonteOpt;
    }

    public List<FormComprobacionHorizonteOpt> validateFlecked(Integer idFormComprobacion){
        List<FormComprobacionHorizonteOpt> listFormComprobacionHorizonteOpt = new LinkedList<>();
        for(View row:viewListFlecked){
            FormComprobacionHorizonteOpt entity = new FormComprobacionHorizonteOpt();
            entity.setIdFormComprobacionHorz(idFormComprobacion);
            LinearLayout child = (LinearLayout)row;
            for(int index = 0; index < child.getChildCount(); index++) {
                View object = child.getChildAt(index);
                if (object instanceof LinearLayout && object.getId() == R.id.lyr_colors) {
                    LinearLayout lyr = (LinearLayout)object;
                    for(int index2 = 0; index2 < lyr.getChildCount(); index2++) {
                        View object2 = lyr.getChildAt(index2);
                        if (object2 instanceof Spinner) {
                            Spinner spinner = (Spinner) object2;
                            switch (spinner.getId()) {
                                case R.id.input_color_hue:
                                    entity.setColorHue(((Domain) spinner.getSelectedItem()).getId());
                                    break;
                                case R.id.input_color_value:
                                    entity.setColorValue(((Domain) spinner.getSelectedItem()).getId());
                                    break;
                                case R.id.input_color_chroma:
                                    entity.setColorChroma(((Domain) spinner.getSelectedItem()).getId());
                                    break;
                            }
                        }
                    }
                } else if (object instanceof EditText) {
                    EditText editText = (EditText) object;
                    if (editText.getId() == R.id.input_color_percent) {
                        try {
                            entity.setColorPorcentaje(Integer.valueOf(editText.getText().toString()));
                        } catch (Exception ex) {
                            entity.setColorPorcentaje(null);
                        }
                    }
                }
            }
            listFormComprobacionHorizonteOpt.add(entity);
        }
        return listFormComprobacionHorizonteOpt;
    }

    public void nextAction() {
        String depth = inputDepth.getText().toString();
        Domain colorHue = ((Domain)inputColorHue.getSelectedItem());
        Domain colorValue = ((Domain)inputColorValue.getSelectedItem());
        Domain colorChroma = ((Domain)inputColorChroma.getSelectedItem());
        String colorPercent = inputColorPercent.getText().toString();
        Domain materialType = ((Domain) inputMaterialType.getSelectedItem());
        Domain texture = new Domain();
        if(materialType.getCodigo()!=null) {
            if (materialType.getCodigo().equals("1")) {
                texture = ((Domain) inputTexture.getSelectedItem());
            } else {
                texture = ((Domain) inputOrganicType.getSelectedItem());
            }
        }

        Domain texturaClase = ((Domain) inputTexture.getSelectedItem());
        Domain textureModifiers = ((Domain) inputTextureModifiers.getSelectedItem());
        String texturePercent = inputTexturePercent.getText().toString();
        Domain estructuraTipo = ((Domain) inputStructure.getSelectedItem());
        Domain estructuraClase = ((Domain) inputStructureType.getSelectedItem());
        Domain estructuraGrado = ((Domain) inputStructureType2.getSelectedItem());
        Domain formaRompe = ((Domain) inputStructureForm.getSelectedItem());
        Domain motivoNoEstructura = ((Domain) inputStructureReasons.getSelectedItem());
        String estructuraOtra = inputStructureTypeOther.getText().toString();
        Domain organicoClase = ((Domain) inputOrganicType.getSelectedItem());
        Domain organicoComposicion = ((Domain) inputOrganicType2.getSelectedItem());
        Domain skylineType = ((Domain) inputSkylineType.getSelectedItem());
        Domain horizonteCaracterisitica = ((Domain) inputHorizonteCaracterisitica.getSelectedItem());

        if(!depth.isEmpty() && colorHue.getId()!=null && colorValue.getId()!=null
                && colorChroma.getId()!=null
                && texture.getId()!=null) {

            List<FormComprobacionHorizonteOpt> listFormComprobacionHorizonteOpt = validateOptional(null);
            boolean optionalFlag = true;
            if(listFormComprobacionHorizonteOpt.size() > 0){
                for(FormComprobacionHorizonteOpt row:listFormComprobacionHorizonteOpt){
                    if(row.getColorHue() == null || row.getColorChroma() == null
                            || row.getColorValue() == null || row.getColorPorcentaje() ==null) {
                        optionalFlag = false;
                        break;
                    }
                }
            }

            List<FormComprobacionHorizonteOpt> listFormComprobacionHorizonteFlecked = validateFlecked(null);
            boolean fleckedFlag = true;
            if(listFormComprobacionHorizonteFlecked.size() > 0){
                for(FormComprobacionHorizonteOpt row:listFormComprobacionHorizonteFlecked){
                    if(row.getColorHue() == null || row.getColorChroma() == null
                            || row.getColorValue() == null || row.getColorPorcentaje() ==null) {
                        fleckedFlag = false;
                        break;
                    }
                }
            }

            if(optionalFlag && fleckedFlag) {
                btnPreview.setEnabled(true);
                btnPreviewTop.setEnabled(true);
                index++;
                if (mode == Modes.NEW) {
                    formComprobacionHorizonte = new FormComprobacionHorizonte();
                    setValues(depth, colorHue, colorValue, colorChroma, colorPercent,
                            materialType, texturaClase, textureModifiers, texturePercent,
                            organicoClase, organicoComposicion,
                            estructuraTipo, estructuraClase, estructuraGrado, formaRompe, motivoNoEstructura,
                            estructuraOtra, skylineType, horizonteCaracterisitica);
                    formComprobacionHorizonteList.add(formComprobacionHorizonte);
                    if(formComprobacionHorizonteList.size() > 1) {
                        labelEndopedones.setVisibility(View.VISIBLE);
                        inputEndopedones.setVisibility(View.VISIBLE);
                    }
                    listOptionalEntity.add(new OptionalEntity(index-1, viewListOptional, viewListFlecked));
                } else {
                    setValues(depth, colorHue, colorValue, colorChroma, colorPercent,
                            materialType, texturaClase, textureModifiers, texturePercent,
                            organicoClase, organicoComposicion,
                            estructuraTipo, estructuraClase, estructuraGrado, formaRompe, motivoNoEstructura,
                            estructuraOtra, skylineType, horizonteCaracterisitica);
                }
                getValues();

                FormComprobacionHorizonteAdapter skylineAdapter = new FormComprobacionHorizonteAdapter(getActivity(),
                        getFragmentManager(), R.layout.list_skyline_item, formComprobacionHorizonteList);
                listViewSkyline.setItemsCanFocus(false);
                listViewSkyline.setAdapter(skylineAdapter);
            } else {
                Toast.makeText(getActivity(),
                        "Los campos marcados con * son obligatorios en horizontes opcionales y moteados", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            Toast.makeText(getActivity(),
                    "Los campos marcados con * son obligatorios. Sin embargo por ahora es obligatorio Profundidad, Color" +
                            " y Textura", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void saveAction(){
        String nroObservacion = inputNroObservacion.getText().toString();
        Integer reconocedor = ((Domain)inputReconocedor.getSelectedItem()).getId();
        String nombreSitio = inputNombreSitio.getText().toString();
        String fechaHora = inputFecha.getText().toString();
        Integer epocaClimatica = ((Domain)inputEpoca.getSelectedItem()).getId();
        String epocaDias = inputEpocaDias.getText().toString();
        Integer pendienteLongitud = ((Domain)inputLongitud.getSelectedItem()).getId();
        Integer gradoErosion = ((Domain)inputErosion.getSelectedItem()).getId();
        Integer tipoMovimiento = ((Domain)inputMovimiento.getSelectedItem()).getId();
        Integer anegamiento = ((Domain)inputAnegamiento.getSelectedItem()).getId();
        Integer frecuencia = ((Domain)inputFrecuencia.getSelectedItem()).getId();
        Integer duracion = ((Domain)inputDuracion.getSelectedItem()).getId();
        Integer pedregosidad = ((Domain)inputPedegrosidad.getSelectedItem()).getId();
        Integer afloramiento = ((Domain)inputAfloramiento.getSelectedItem()).getId();
        Integer fragmentoSuelo = ((Domain) inputFragmentoSuelo.getSelectedItem()).getId();
        Integer drenajeNatural = ((Domain)inputDrenajeNatural.getSelectedItem()).getId();
        Integer profundidadEfectiva = ((Domain)inputProfundidadEfectiva.getSelectedItem()).getId();
        Integer epidedones = ((Domain)inputEpidedones.getSelectedItem()).getId();
        Integer endopedones = ((Domain)inputEndopedones.getSelectedItem()).getId();

        if(reconocedor == null || nombreSitio.isEmpty() || epocaClimatica == null ||
                pendienteLongitud == null || gradoErosion == null || tipoMovimiento == null || anegamiento == null ||
                pedregosidad == null  || afloramiento == null || fragmentoSuelo == null ||
                drenajeNatural == null || profundidadEfectiva == null || epidedones == null ||
                (inputEndopedones.getVisibility() == View.VISIBLE && endopedones == null) || formComprobacionHorizonteList.isEmpty() || imgViewPic.getDrawable()==null) {
            Toast.makeText(getActivity(), "Los campos marcados con * son obligatorios!", Toast.LENGTH_SHORT).show();
        } else {
            if(formComprobacion == null || mode == Modes.NEW) {
                formComprobacion = new FormComprobacion();
                formComprobacion.setLongitud(Double.valueOf(Singleton.getInstance().getX()));
                formComprobacion.setLatitud(Double.valueOf(Singleton.getInstance().getY()));
                formComprobacion.setFechaHora(fechaHora);
                formComprobacion.setAltitud(null);
                formComprobacion.setNroObservacion(nroObservacion);
                formComprobacionFoto = new FormComprobacionFoto();
            }

            formComprobacion.setReconocedor(reconocedor);
            formComprobacion.setEpocaClimatica(epocaClimatica);
            formComprobacion.setDiasLluvia(epocaDias);
            formComprobacion.setPendienteLongitud(pendienteLongitud);
            formComprobacion.setGradoErosion(gradoErosion);
            formComprobacion.setTipoMovimiento(tipoMovimiento);
            formComprobacion.setAnegamiento(anegamiento);
            formComprobacion.setFrecuencia(frecuencia);
            formComprobacion.setDuracion(duracion);
            formComprobacion.setPedregosidad(pedregosidad);
            formComprobacion.setAfloramiento(afloramiento);
            formComprobacion.setFragmentoSuelo(fragmentoSuelo);
            formComprobacion.setDrenajeNatural(drenajeNatural);
            formComprobacion.setProfundidadEfectiva(profundidadEfectiva);
            formComprobacion.setEpidedones(epidedones);
            formComprobacion.setEndopedones(endopedones);
            formComprobacion.setEstado(1);

            DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
            Long result = dataBaseHelper.insertFormComprobacion(formComprobacion);
            if(result > 0) {
                Bitmap bitmap = ((BitmapDrawable)imgViewPic.getDrawable()).getBitmap();
                formComprobacionFoto.setIdFormComprobacion(result.intValue());
                //formComprobacionFoto.setFoto(DBBitmapUtility.getBytes(bitmap));

                String nameFile = "img_" + inputNroObservacion.getText().toString() + ".jpeg";
                DBBitmapUtility.saveImage(getActivity(), bitmap, nameFile);
                formComprobacionFoto.setFoto(nameFile);
                dataBaseHelper.insertFormComprobacionFoto(formComprobacionFoto);

                int i = 0;
                for(FormComprobacionHorizonte row:formComprobacionHorizonteList){
                    row.setIdFormComprobacion(result.intValue());
                    Long id = dataBaseHelper.insertFormComprobacionHorizonte(row);
                    row.setId(id.intValue());

                    for(OptionalEntity optionalEntity : listOptionalEntity) {
                        if(optionalEntity.getIndex() == i) {
                            viewListOptional = optionalEntity.getViewListOptional();
                            List<FormComprobacionHorizonteOpt> listFormComprobacionHorizonteOptional = validateOptional(id.intValue());
                            for(FormComprobacionHorizonteOpt row2:listFormComprobacionHorizonteOptional){
                                Long id2 = dataBaseHelper.insertFormComprobacionHorizonteOptional(row2);
                                row2.setId(id2.intValue());
                            }

                            viewListFlecked = optionalEntity.getViewListFlecked();
                            List<FormComprobacionHorizonteOpt> listFormComprobacionHorizonteFlecked = validateFlecked(id.intValue());
                            for(FormComprobacionHorizonteOpt row2:listFormComprobacionHorizonteFlecked){
                                Long id2 = dataBaseHelper.insertFormComprobacionHorizonteFlecked(row2);
                                row2.setId(id2.intValue());
                            }
                            break;
                        }
                    }

                    i++;
                }
                mode = Modes.EDIT;
            }

            Toast.makeText(getActivity(), "Formulario " + formComprobacion.getNroObservacion()
                    + " actualizado correctamente!", Toast.LENGTH_SHORT).show();
        }
    }

    void clearAnegamiento() {
        inputFrecuencia.setSelection(0);
        inputDuracion.setSelection(0);
    }

    void clearFields() {
        linearLayoutOpt.removeAllViews();
        indexOptional = 0;
        indexFlecked = 0;
        viewListOptional = new LinkedList<>();
        viewListFlecked = new LinkedList<>();

        inputDepth.setText(null);
        inputColorHue.setSelection(0);
        inputColorValue.setSelection(0);
        inputColorChroma.setSelection(0);
        inputColorPercent.setText(null);
        inputMaterialType.setSelection(0);
        inputTexturePercent.setText(null);
        inputSkylineType.setSelection(0);
        inputHorizonteCaracterisitica.setSelection(0);

        clearStructureFields();
        clearOrganicFields();
    }

    void clearStructureFields() {
        inputTexture.setSelection(0);
        inputTextureModifiers.setSelection(0);
        inputStructure.setSelection(0);
        inputStructureType.setSelection(0);
        inputStructureType2.setSelection(0);
        inputStructureTypeOther.setText(null);
        inputStructureForm.setSelection(0);
        inputStructureReasons.setSelection(0);
    }

    void clearOrganicFields() {
        inputOrganicType.setSelection(0);
        inputOrganicType2.setSelection(0);
        inputTextureOther.setText(null);
    }

    @SuppressLint("SetTextI18n")
    void getValues() {
        labelHorizonteNro.setText(String.format(getResources().getString(R.string.tst_horizonte_nro),(index + 1)));
        try{
            formComprobacionHorizonte = formComprobacionHorizonteList.get(index);
            if (formComprobacionHorizonte != null) {
                labelHorizonteNro.setText(String.format(getResources().getString(R.string.tst_horizonte_nro),(index + 1)));
                inputDepth.setText(formComprobacionHorizonte.getProfundidad().toString());
                inputColorPercent.setText(formComprobacionHorizonte.getColorPorcentaje()==null?null:formComprobacionHorizonte.getColorPorcentaje().toString());
                inputTexturePercent.setText(formComprobacionHorizonte.getTexturaPorcentaje()==null?null:formComprobacionHorizonte.getTexturaPorcentaje().toString());
                int spinnerPosition = colorHueAdapter.getPosition(Utils.getDomain(listColorHue, formComprobacionHorizonte.getColorHue()));
                inputColorHue.setSelection(spinnerPosition);
                spinnerPosition = colorValueAdapter.getPosition(Utils.getDomain(listColorValue, formComprobacionHorizonte.getColorValue()));
                inputColorValue.setSelection(spinnerPosition);
                spinnerPosition = colorChromaAdapter.getPosition(Utils.getDomain(listColorChroma, formComprobacionHorizonte.getColorChroma()));
                inputColorChroma.setSelection(spinnerPosition);
                spinnerPosition = materialTypeAdapter.getPosition(Utils.getDomain(listMaterialType, formComprobacionHorizonte.getTipoMaterial()));
                inputMaterialType.setSelection(spinnerPosition);
                spinnerPosition = textureAdapter.getPosition(Utils.getDomain(listTexture, formComprobacionHorizonte.getClaseTextural()));
                inputTexture.setSelection(spinnerPosition);
                spinnerPosition = textureModifiersAdapter.getPosition(Utils.getDomain(listTextureModifiers, formComprobacionHorizonte.getModificadorTextura()));
                inputTextureModifiers.setSelection(spinnerPosition);
                spinnerPosition = organicTypeAdapter.getPosition(Utils.getDomain(listOrganicType, formComprobacionHorizonte.getClaseOrganico()));
                inputOrganicType.setSelection(spinnerPosition);
                spinnerPosition = organicTypeAdapter2.getPosition(Utils.getDomain(listOrganicType2, formComprobacionHorizonte.getClaseComposicion()));
                inputOrganicType2.setSelection(spinnerPosition);
                spinnerPosition = structureAdapter.getPosition(Utils.getDomain(listStructure, formComprobacionHorizonte.getEstructuraTipo()));
                inputStructure.setSelection(spinnerPosition);
                spinnerPosition = structureTypeAdapter.getPosition(Utils.getDomain(listStructureType, formComprobacionHorizonte.getEstructuraClase()));
                inputStructureType.setSelection(spinnerPosition);
                spinnerPosition = structureTypeAdapter2.getPosition(Utils.getDomain(listStructureType2, formComprobacionHorizonte.getEstructuraGrado()));
                inputStructureType2.setSelection(spinnerPosition);
                spinnerPosition = structureFormsAdapter.getPosition(Utils.getDomain(listStructureForms, formComprobacionHorizonte.getFormaRompe()));
                inputStructureForm.setSelection(spinnerPosition);
                spinnerPosition = structureReasonsAdapter.getPosition(Utils.getDomain(listStructureReasons, formComprobacionHorizonte.getMotivoNoEstructura()));
                inputStructureReasons.setSelection(spinnerPosition);
                spinnerPosition = horizontesMaestrosAdapter.getPosition(Utils.getDomain(listHorizonteClase, formComprobacionHorizonte.getHorizonteClase()));
                inputStructureTypeOther.setText(formComprobacionHorizonte.getEstructuraOtra()==null?null:formComprobacionHorizonte.getEstructuraOtra().toString());
                inputSkylineType.setSelection(spinnerPosition);
                spinnerPosition = horizonteCaracterisiticaAdapter.getPosition(Utils.getDomain(listHorizonteCaracterisitica, formComprobacionHorizonte.getHorizonteCaracterisitica()));
                inputHorizonteCaracterisitica.setSelection(spinnerPosition);
                mode = Modes.EDIT;
                nextFlag = false;
                nextFlagMaterialType = false;
                nextFlagEstructuraTipo = false;

                linearLayoutOpt.removeAllViews();
                for(OptionalEntity optionalEntity : listOptionalEntity) {
                    if(optionalEntity.getIndex() == index) {
                        viewListOptional = optionalEntity.getViewListOptional();
                        indexOptional = viewListOptional.size();
                        viewListFlecked = optionalEntity.getViewListFlecked();
                        indexFlecked = viewListFlecked.size();
                        for (View view : viewListOptional) {
                            linearLayoutOpt.addView(view);
                        }
                        for (View view : viewListFlecked) {
                            linearLayoutOpt.addView(view);
                        }
                        break;
                    }
                }
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
                   Domain materialType, Domain texturaClase, Domain texturaModificador, String texturaPorcentaje,
                   Domain organicoClase, Domain organicoComposicion,
                   Domain estructuraTipo, Domain estructuraClase, Domain estructuraGrado, Domain formaRompe,
                   Domain motivoNoEstructura, String estructuraOtra,
                   Domain skylineType, Domain horizonteCaracterisitica) {
        formComprobacionHorizonte.setNumeroHorizonte(index);
        formComprobacionHorizonte.setProfundidad(Integer.valueOf(depth));
        formComprobacionHorizonte.setColorHue(colorHue.getId());
        formComprobacionHorizonte.setDescColorHue(colorHue.getDescripcion());
        formComprobacionHorizonte.setColorValue(colorValue.getId());
        formComprobacionHorizonte.setDescColorValue(colorValue.getDescripcion());
        formComprobacionHorizonte.setColorChroma(colorChroma.getId());
        formComprobacionHorizonte.setDescColorChroma(colorChroma.getDescripcion());
        formComprobacionHorizonte.setColorPorcentaje(colorPercent.isEmpty()?null:Integer.valueOf(colorPercent));
        formComprobacionHorizonte.setTipoMaterial(materialType.getId());
        formComprobacionHorizonte.setDescTipoMaterial(materialType.getDescripcion());
        formComprobacionHorizonte.setClaseTextural(texturaClase!=null?texturaClase.getId():null);
        formComprobacionHorizonte.setModificadorTextura(texturaModificador!=null?texturaClase.getId():null);
        formComprobacionHorizonte.setTexturaPorcentaje(texturaPorcentaje.isEmpty()?null:Integer.valueOf(texturaPorcentaje));
        formComprobacionHorizonte.setClaseOrganico(organicoClase!=null?organicoClase.getId():null);
        formComprobacionHorizonte.setClaseComposicion(organicoComposicion!=null?organicoComposicion.getId():null);
        formComprobacionHorizonte.setEstructuraTipo(estructuraTipo!=null?estructuraTipo.getId():null);
        formComprobacionHorizonte.setEstructuraClase(estructuraClase!=null?estructuraClase.getId():null);
        formComprobacionHorizonte.setEstructuraGrado(estructuraGrado!=null?estructuraGrado.getId():null);
        formComprobacionHorizonte.setFormaRompe(formaRompe!=null?formaRompe.getId():null);
        formComprobacionHorizonte.setMotivoNoEstructura(motivoNoEstructura!=null?motivoNoEstructura.getId():null);
        formComprobacionHorizonte.setEstructuraOtra(estructuraOtra);
        formComprobacionHorizonte.setHorizonteClase(skylineType.getId());
        formComprobacionHorizonte.setHorizonteCaracterisitica(horizonteCaracterisitica.getId());
    }

    public void removeFormComprobacionHorizonte() {
        if(formComprobacionHorizonteList.isEmpty()) {
            clearFields();
            btnPreview.setEnabled(false);
            btnPreviewTop.setEnabled(false);
            index = 0;
            mode = Modes.NEW;
            labelHorizonteNro.setText(String.format(getResources().getString(R.string.tst_horizonte_nro),(index + 1)));
        } else {
            int i = 0;
            for(FormComprobacionHorizonte row: formComprobacionHorizonteList) {
                row.setNumeroHorizonte(i++);
            }
            index = formComprobacionHorizonteList.size() - 1;
            getValues();
        }
    }

    public void editFormComprobacionHorizonte(int position) {
        index = position;
        getValues();
        host.setCurrentTab(2);
    }

    @SuppressLint("InflateParams")
    public void addOptionalView(SkylineType skylineType){
        Integer indexType;
        int type;

        View rootView = this.inflater.inflate(R.layout.tab_testing_skyline, null);
        linearLayoutOpt.addView(rootView);
        TextView labelHorizonteNroOpt = (TextView) rootView.findViewById(R.id.label_horizonte_nro);
        if(skylineType.equals(SkylineType.OPTIONAL)) {
            indexType = ++indexOptional;
            rootView.setId(indexType);
            viewListOptional.add(rootView);
            type = R.string.tst_skyline_optional;
        } else {
            indexType = ++indexFlecked;
            rootView.setId(indexType);
            viewListFlecked.add(rootView);
            type = R.string.tst_skyline_flecked;
        }

        labelHorizonteNroOpt.setText(String.format(getResources().getString(type), indexType));
        final Button btnDeleteOptional = (Button) rootView.findViewById(R.id.btn_delete_optional);
        btnDeleteOptional.setTransformationMethod(null);
        btnDeleteOptional.setTag(skylineType+"|"+indexType);
        btnDeleteOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tag = v.getTag().toString().split("\\|");
                int tagIndex = Integer.valueOf(tag[1]);
                if(tag[0].equals(SkylineType.OPTIONAL.name())) {
                    for(View row:viewListOptional) {
                        if(row.getId() == tagIndex) {
                            viewListOptional.remove(row);
                            linearLayoutOpt.removeView(row);
                            indexOptional--;
                            break;
                        }
                    }
                } else {
                    for(View row:viewListFlecked) {
                        if(row.getId() == tagIndex) {
                            viewListFlecked.remove(row);
                            linearLayoutOpt.removeView(row);
                            indexFlecked--;
                            break;
                        }
                    }
                }
            }
        });

        final Spinner inputColorHue = (Spinner) rootView.findViewById(R.id.input_color_hue);
        final Spinner inputColorValue = (Spinner) rootView.findViewById(R.id.input_color_value);
        final Spinner inputColorChroma = (Spinner) rootView.findViewById(R.id.input_color_chroma);

        colorHueAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorHue);
        inputColorHue.setAdapter(colorHueAdapter);

        colorValueAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorValue);
        inputColorValue.setAdapter(colorValueAdapter);

        colorChromaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorChroma);
        inputColorChroma.setAdapter(colorChromaAdapter);
    }

    public View addOptionalView(SkylineType skylineType, Integer index,
                                Integer colorHue, Integer colorValue, Integer colorChroma,
                                String colorPercent){
        View rootView = this.inflater.inflate(R.layout.tab_testing_skyline, null);
        linearLayoutOpt.addView(rootView);
        TextView labelHorizonteNroOpt = (TextView) rootView.findViewById(R.id.label_horizonte_nro);
        if(skylineType.equals(SkylineType.OPTIONAL)) {
            rootView.setId(indexOptional++);
            labelHorizonteNroOpt.setText(String.format(getResources().getString(R.string.tst_skyline_optional), index + 1));
            viewListOptional.add(rootView);
        } else {
            rootView.setId(indexFlecked++);
            labelHorizonteNroOpt.setText(String.format(getResources().getString(R.string.tst_skyline_flecked), index + 1));
            viewListFlecked.add(rootView);
        }

        final Spinner inputColorHue = (Spinner) rootView.findViewById(R.id.input_color_hue);
        final Spinner inputColorValue = (Spinner) rootView.findViewById(R.id.input_color_value);
        final Spinner inputColorChroma = (Spinner) rootView.findViewById(R.id.input_color_chroma);
        final EditText inputColorPercent = (EditText) rootView.findViewById(R.id.input_color_percent);

        colorHueAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorHue);
        inputColorHue.setAdapter(colorHueAdapter);

        colorValueAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorValue);
        inputColorValue.setAdapter(colorValueAdapter);

        colorChromaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listColorChroma);
        inputColorChroma.setAdapter(colorChromaAdapter);

        int spinnerPosition = colorHueAdapter.getPosition(Utils.getDomain(listColorHue, colorHue));
        inputColorHue.setSelection(spinnerPosition);
        spinnerPosition = colorValueAdapter.getPosition(Utils.getDomain(listColorValue, colorValue));
        inputColorValue.setSelection(spinnerPosition);
        spinnerPosition = colorChromaAdapter.getPosition(Utils.getDomain(listColorChroma, colorChroma));
        inputColorChroma.setSelection(spinnerPosition);
        inputColorPercent.setText(colorPercent);

        return rootView;
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
        if (cursor != null) {
            cursor.moveToFirst();
        }

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

    private void bottomScrollView() {
        View lastChild = scrollviewProfile.getChildAt(scrollviewProfile.getChildCount() - 1);
        int bottom = lastChild.getBottom() + scrollviewProfile.getPaddingBottom();
        int sy = scrollviewProfile.getScrollY();
        int sh = scrollviewProfile.getHeight();
        int delta = bottom + (sy + sh);
        scrollviewProfile.scrollTo(0, delta);
    }

    public void editForm(){
        if(formComprobacion == null){
            formComprobacion = new FormComprobacion();
            inputNroObservacion.setText(dataBaseHelper.getNroObservacion("C"));
            inputFecha.setText(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            labelHorizonteNro.setText(String.format(getResources().getString(R.string.tst_horizonte_nro), (index + 1)));
            mode = Modes.NEW;
        } else {
            inputNroObservacion.setText(formComprobacion.getNroObservacion());
            inputFecha.setText(formComprobacion.getFechaHora());
            int spinnerPosition = reconocedorAdapter.getPosition(Utils.getDomain(listReconocedor, formComprobacion.getReconocedor()));
            inputReconocedor.setSelection(spinnerPosition);
            spinnerPosition = epocaAdapter.getPosition(Utils.getDomain(listEpoca, formComprobacion.getEpocaClimatica()));
            inputEpoca.setSelection(spinnerPosition);
            inputEpocaDias.setText(formComprobacion.getDiasLluvia());
            spinnerPosition = longitudAdapter.getPosition(Utils.getDomain(listLongitud, formComprobacion.getPendienteLongitud()));
            inputLongitud.setSelection(spinnerPosition);
            spinnerPosition = erosionAdapter.getPosition(Utils.getDomain(listErosion, formComprobacion.getGradoErosion()));
            inputErosion.setSelection(spinnerPosition);
            spinnerPosition = movimientoAdapter.getPosition(Utils.getDomain(listMovimiento, formComprobacion.getTipoMovimiento()));
            inputMovimiento.setSelection(spinnerPosition);
            spinnerPosition = anegamientoAdapter.getPosition(Utils.getDomain(listAnegamiento, formComprobacion.getAnegamiento()));
            inputAnegamiento.setSelection(spinnerPosition);
            spinnerPosition = frecuenciaAdapter.getPosition(Utils.getDomain(listFrecuencia, formComprobacion.getFrecuencia()));
            inputFrecuencia.setSelection(spinnerPosition);
            spinnerPosition = duracionAdapter.getPosition(Utils.getDomain(listDuracion, formComprobacion.getDuracion()));
            inputDuracion.setSelection(spinnerPosition);
            spinnerPosition = pedegrosidadAdapter.getPosition(Utils.getDomain(listPedegrosidad, formComprobacion.getPedregosidad()));
            inputPedegrosidad.setSelection(spinnerPosition);
            spinnerPosition = afloramientoAdapter.getPosition(Utils.getDomain(listAfloramiento, formComprobacion.getAfloramiento()));
            inputAfloramiento.setSelection(spinnerPosition);
            spinnerPosition = soilFragmentAdapter.getPosition(Utils.getDomain(listSoilFragment, formComprobacion.getFragmentoSuelo()));
            inputFragmentoSuelo.setSelection(spinnerPosition);
            spinnerPosition = naturalDrainageAdapter.getPosition(Utils.getDomain(listNaturalDrainage, formComprobacion.getDrenajeNatural()));
            inputDrenajeNatural.setSelection(spinnerPosition);
            spinnerPosition = effectiveDepthAdapter.getPosition(Utils.getDomain(listEffectiveDepth, formComprobacion.getProfundidadEfectiva()));
            inputProfundidadEfectiva.setSelection(spinnerPosition);
            spinnerPosition = epidedonesAdapter.getPosition(Utils.getDomain(listEpidedones, formComprobacion.getEpidedones()));
            inputEpidedones.setSelection(spinnerPosition);
            spinnerPosition = endopedonesAdapter.getPosition(Utils.getDomain(listEndopedones, formComprobacion.getEndopedones()));
            inputEndopedones.setSelection(spinnerPosition);

            formComprobacionFoto = dataBaseHelper.getFormComprobacionFoto(formComprobacion.getId());
            //Bitmap bitmap = DBBitmapUtility.getImage(formComprobacionFoto.getFoto());
            Bitmap bitmap = DBBitmapUtility.loadImageBitmap(getActivity(), formComprobacionFoto.getFoto());
            imgViewPic.setImageBitmap(bitmap);

            formComprobacionHorizonteList = dataBaseHelper.getListFormComprobacionHorizonte(formComprobacion.getId());
            if (formComprobacionHorizonteList != null && !formComprobacionHorizonteList.isEmpty()) {
                FormComprobacionHorizonteAdapter skylineAdapter = new FormComprobacionHorizonteAdapter(getActivity(),
                        getFragmentManager(), R.layout.list_skyline_item, formComprobacionHorizonteList);
                listViewSkyline.setItemsCanFocus(false);
                listViewSkyline.setAdapter(skylineAdapter);
                index = 0;
                getValues();
            }

            int i = 0;
            for (FormComprobacionHorizonte row : formComprobacionHorizonteList) {
                List<View> viewListOptional = new LinkedList<>();
                List<View> viewListFlecked = new LinkedList<>();

                List<FormComprobacionHorizonteOpt> listFormComprobacionHorizonteOptional =
                        dataBaseHelper.getListFormComprobacionHorizonteOptional(row.getId());
                int j = 0;
                for(FormComprobacionHorizonteOpt opt:listFormComprobacionHorizonteOptional) {
                    View view = addOptionalView(SkylineType.OPTIONAL, j, opt.getColorHue(),
                            opt.getColorValue(), opt.getColorChroma(), String.valueOf(opt.getColorPorcentaje()));
                    viewListOptional.add(view);
                    j++;
                }

                List<FormComprobacionHorizonteOpt> listFormComprobacionHorizonteFlecked =
                        dataBaseHelper.getListFormComprobacionHorizonteFlecked(row.getId());
                j = 0;
                for(FormComprobacionHorizonteOpt opt:listFormComprobacionHorizonteFlecked){
                    View view = addOptionalView(SkylineType.OPTIONAL, j, opt.getColorHue(),
                            opt.getColorValue(), opt.getColorChroma(), String.valueOf(opt.getColorPorcentaje()));
                    viewListFlecked.add(view);
                    j++;
                }

                OptionalEntity optionalEntity = new OptionalEntity();
                optionalEntity.setIndex(i);
                optionalEntity.setViewListOptional(viewListOptional);
                optionalEntity.setViewListFlecked(viewListFlecked);
                listOptionalEntity.add(optionalEntity);
                i++;
            }

            nextFlag = false;
            nextFlagMaterialType = false;
            nextFlagEstructuraTipo = false;
        }
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        Context context;

        public MyTask(ProgressDialog progressDialog, Context context) {
            this.progressDialog = progressDialog;
            this.context = context;
        }

        public void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            //aquí se puede colocar código a ejecutarse previo a la operación
        }

        public void onPostExecute(Void unused) {
        //aquí se puede colocar código que
        //se ejecutará tras finalizar
            progressDialog.dismiss();
        }

        protected Void doInBackground(Void... params) {
            //realizar la operación aquí
            return null;
        }
    }

}
