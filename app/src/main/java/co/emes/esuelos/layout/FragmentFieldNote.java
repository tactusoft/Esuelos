package co.emes.esuelos.layout;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.emes.esuelos.R;
import co.emes.esuelos.model.Domain;
import co.emes.esuelos.model.FormNotaCampo;
import co.emes.esuelos.model.FormNotaCampoFoto;
import co.emes.esuelos.util.DBBitmapUtility;
import co.emes.esuelos.util.DataBaseHelper;
import co.emes.esuelos.util.Singleton;
import co.emes.esuelos.util.Utils;

/**
 * Created by csarmiento on 11/14/16
 */
public class FragmentFieldNote extends DialogFragment implements View.OnClickListener {

    public static final String TAG = FragmentFieldNote.class.getSimpleName();

    LayoutInflater inflater;
    TabHost tabHost;
    EditText inputNroObservacion;
    EditText inputFecha;
    Spinner inputReconocedor;
    EditText inputNombreSitio;
    Spinner inputEpoca;
    EditText inputEpocaDias;
    Spinner inputGradiente;
    Spinner inputPendienteLongitud;
    Spinner inputPendienteForma;
    Spinner inputClaseErosion;
    Spinner inputTipoErosion;
    Spinner inputGradoErosion;
    Spinner inputClasesMovimiento;
    Spinner inputTiposMovimiento;
    Spinner inputFrecuenciasMovimiento;
    Spinner inputAnegamiento;
    TextView labelFrecuencia;
    Spinner inputFrecuencia;
    TextView labelDuracion;
    Spinner inputDuracion;
    Spinner inputPedegrosidad;
    Spinner inputAfloramiento;
    Spinner inputGrupoUso;
    TextView labelSubgrupoUso;
    Spinner inputSubgrupoUso;
    TextView labelNombreCultivo;
    EditText inputNombreCultivo;
    ImageView imgViewPic;

    Bitmap foto;
    String nroObservacion;
    Integer reconocedor;
    String nombreSitio;
    String fechaHora;
    Integer epocaClimatica;
    String epocaDias;
    Integer gradiente;
    Integer pendienteLongitud;
    Integer pendienteForma;
    Integer claseErosion;
    Integer tipoErosion;
    Integer gradoErosion;
    Integer clasesMovimiento;
    Integer tiposMovimiento;
    Integer gradosMovimiento;
    Integer anegamiento;
    Integer frecuencia;
    Integer duracion;
    Integer pedregosidad;
    Integer afloramiento;
    Integer grupoUso;
    Integer subgrupoUso;

    List<Domain> listReconocedor;
    List<Domain> listEpoca;
    List<Domain> listGradiente;
    List<Domain> listPendienteLongitud;
    List<Domain> listPendienteForma;
    List<Domain> listClaseErosion;
    List<Domain> listTipoErosion;
    List<Domain> listGradoErosion;
    List<Domain> listClasesMovimiento;
    List<Domain> listTiposMovimiento;
    List<Domain> listFrecuenciasMovimiento;
    List<Domain> listAnegamiento;
    List<Domain> listFrecuencia;
    List<Domain> listDuracion;
    List<Domain> listPedegrosidad;
    List<Domain> listAfloramiento;
    List<Domain> listGrupoUso;
    List<Domain> listSubgrupoUso;

    ArrayAdapter<Domain> reconocedorAdapter;
    ArrayAdapter<Domain> epocaAdapter;
    ArrayAdapter<Domain> gradienteAdapter;
    ArrayAdapter<Domain> pendienteLongitudAdapter;
    ArrayAdapter<Domain> pendienteFormaAdapter;
    ArrayAdapter<Domain> claseErosionAdapter;
    ArrayAdapter<Domain> tipoErosionAdapter;
    ArrayAdapter<Domain> gradoErosionAdapter;
    ArrayAdapter<Domain> clasesMovimientoAdapter;
    ArrayAdapter<Domain> tiposMovimientoAdapter;
    ArrayAdapter<Domain> frecuenciasMovimientoAdapter;
    ArrayAdapter<Domain> anegamientoAdapter;
    ArrayAdapter<Domain> frecuenciaAdapter;
    ArrayAdapter<Domain> duracionAdapter;
    ArrayAdapter<Domain> pedegrosidadAdapter;
    ArrayAdapter<Domain> afloramientoAdapter;

    ArrayAdapter<Domain> grupoUsoAdapter;
    ArrayAdapter<Domain> subgrupoUsoAdapter;

    private static final int RESULT_OK = -1;
    public static final int RESULT_LOAD_IMAGE = 1000;
    public static final int REQUEST_IMAGE_CAPTURE = 1001;

    DataBaseHelper dataBaseHelper;
    FormNotaCampo formNotaCampo;
    FormNotaCampoFoto formNotaCampoFoto;

    boolean nextFlag = true;
    boolean nextFlagClaseErosion= true;
    boolean nextFlagClaseMovimiento = true;
    boolean nextFlagGrupoUso = true;
    boolean nextFlagSubgrupoUso = true;

    static Modes mode;

    enum Modes {
        NEW, EDIT
    }

    public FragmentFieldNote() {
        super();
        dataBaseHelper =  new DataBaseHelper(getActivity());
        mode = Modes.NEW;
    }

    public static FragmentFieldNote newInstance(FormNotaCampo formNotaCampo) {
        FragmentFieldNote f = new FragmentFieldNote();
        f.setFormNotaCampo(formNotaCampo);
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
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.principal_background)));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_field_note, container, false);
        getDialog().setCancelable(false);

        tabHost = (TabHost)rootView.findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec(getResources().getString(R.string.tst_general));
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.tst_general));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec(getResources().getString(R.string.tst_externa));
        spec.setContent(R.id.tab2);
        spec.setIndicator(getResources().getString(R.string.tst_externa));
        tabHost.addTab(spec);

        //Tab 3
        spec = tabHost.newTabSpec(getResources().getString(R.string.fnd_other));
        spec.setContent(R.id.tab3);
        spec.setIndicator(getResources().getString(R.string.fnd_other));
        tabHost.addTab(spec);

        imgViewPic  = (ImageView) rootView.findViewById(R.id.imgView_pic);

        inputNroObservacion = (EditText) rootView.findViewById(R.id.input_nro_observacion);
        inputFecha = (EditText) rootView.findViewById(R.id.input_fecha);
        inputReconocedor = (Spinner) rootView.findViewById(R.id.input_reconocedor);
        inputNombreSitio = (EditText) rootView.findViewById(R.id.input_nombre_sitio);
        inputEpoca = (Spinner) rootView.findViewById(R.id.input_epoca);
        inputEpocaDias = (EditText) rootView.findViewById(R.id.input_epoca_dias);
        inputGradiente = (Spinner) rootView.findViewById(R.id.input_gradiente);
        inputPendienteLongitud = (Spinner) rootView.findViewById(R.id.input_pendiente_longitud);
        inputPendienteForma = (Spinner) rootView.findViewById(R.id.input_pendiente_forma);
        inputClaseErosion = (Spinner) rootView.findViewById(R.id.input_clase_erosion);
        inputTipoErosion = (Spinner) rootView.findViewById(R.id.input_tipo_erosion);
        inputGradoErosion = (Spinner) rootView.findViewById(R.id.input_grado_erosion);
        inputClasesMovimiento = (Spinner) rootView.findViewById(R.id.input_clases_movimiento);
        inputTiposMovimiento = (Spinner) rootView.findViewById(R.id.input_tipos_movimiento);
        inputFrecuenciasMovimiento = (Spinner) rootView.findViewById(R.id.input_grados_movimiento);
        inputAnegamiento = (Spinner) rootView.findViewById(R.id.input_anegamiento);
        labelFrecuencia = (TextView) rootView.findViewById(R.id.label_frecuencia);
        inputFrecuencia = (Spinner) rootView.findViewById(R.id.input_frecuencia);
        labelDuracion = (TextView) rootView.findViewById(R.id.label_duracion);
        inputDuracion = (Spinner) rootView.findViewById(R.id.input_duracion);
        inputPedegrosidad = (Spinner) rootView.findViewById(R.id.input_pedegrosidad);
        inputAfloramiento = (Spinner) rootView.findViewById(R.id.input_afloramiento);

        inputGrupoUso = (Spinner) rootView.findViewById(R.id.input_grupo_uso);
        labelSubgrupoUso = (TextView) rootView.findViewById(R.id.label_subgrupo_uso);
        inputSubgrupoUso = (Spinner) rootView.findViewById(R.id.input_subgrupo_uso);
        labelNombreCultivo = (TextView) rootView.findViewById(R.id.label_nombre_cultivo);
        inputNombreCultivo = (EditText) rootView.findViewById(R.id.input_nombre_cultivo);

        Button btnSave = (Button) rootView.findViewById(R.id.btn_save);
        Button btnTakePhoto = (Button) rootView.findViewById(R.id.btn_take_photo);
        Button btnSelectPhoto = (Button) rootView.findViewById(R.id.btn_select_photo);

        btnSave.setTransformationMethod(null);
        btnTakePhoto.setTransformationMethod(null);
        btnSelectPhoto.setTransformationMethod(null);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                nextFlag = false;
                nextFlagClaseErosion = false;
                nextFlagClaseMovimiento = false;
                nextFlagGrupoUso = false;
                nextFlagSubgrupoUso = false;
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

        labelFrecuencia.setVisibility(View.GONE);
        labelDuracion.setVisibility(View.GONE);
        inputFrecuencia.setVisibility(View.GONE);
        inputDuracion.setVisibility(View.GONE);
        inputTipoErosion.setVisibility(View.GONE);
        inputGradoErosion.setVisibility(View.GONE);
        inputTiposMovimiento.setVisibility(View.GONE);
        inputFrecuenciasMovimiento.setVisibility(View.GONE);
        labelSubgrupoUso.setVisibility(View.GONE);
        inputSubgrupoUso.setVisibility(View.GONE);
        labelNombreCultivo.setVisibility(View.GONE);
        inputNombreCultivo.setVisibility(View.GONE);

        inputClaseErosion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                int positionTipoErosion = 0;
                if(nextFlagClaseErosion) {
                    clearClaseErosion();
                } else {
                    positionTipoErosion = inputTipoErosion.getSelectedItemPosition();
                }

                Domain domain = (Domain) inputClaseErosion.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (domain.getCodigo().equals("50")) {
                        inputTipoErosion.setVisibility(View.GONE);
                        inputGradoErosion.setVisibility(View.GONE);
                    } else {
                        inputTipoErosion.setVisibility(View.VISIBLE);
                        inputGradoErosion.setVisibility(View.VISIBLE);

                        List<Domain> listDomainTemp = new LinkedList<>();
                        for(Domain row:listTipoErosion){
                            if(row.getValor()==null) {
                                listDomainTemp.add(row);
                            } else if(domain.getCodigo().equals("10")
                                    && Integer.valueOf(row.getCodigo()) >= 11 && Integer.valueOf(row.getCodigo()) <= 19) {
                                listDomainTemp.add(row);
                            } else if(domain.getCodigo().equals("20")
                                    && Integer.valueOf(row.getCodigo()) >= 21 && Integer.valueOf(row.getCodigo()) <= 22) {
                                listDomainTemp.add(row);
                            } else if(domain.getCodigo().equals("30")
                                    && Integer.valueOf(row.getCodigo()) >= 31 && Integer.valueOf(row.getCodigo()) <= 32) {
                                listDomainTemp.add(row);
                            } else if(domain.getCodigo().equals("40")
                                    && Integer.valueOf(row.getCodigo()) >= 41 && Integer.valueOf(row.getCodigo()) <= 42) {
                                listDomainTemp.add(row);
                            }
                        }
                        tipoErosionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listDomainTemp);
                        inputTipoErosion.setAdapter(tipoErosionAdapter);
                        inputTipoErosion.setSelection(positionTipoErosion);
                    }
                } else {
                    inputTipoErosion.setVisibility(View.GONE);
                    inputGradoErosion.setVisibility(View.GONE);
                }

                nextFlagClaseErosion = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        inputClasesMovimiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                int positionTipoMovimiento = 0;
                if(nextFlagClaseMovimiento) {
                    clearClasesMovimiento();
                } else {
                    positionTipoMovimiento = inputClasesMovimiento.getSelectedItemPosition();
                }

                Domain domain = (Domain) inputClasesMovimiento.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (domain.getCodigo().equals("30")) {
                        inputTiposMovimiento.setVisibility(View.GONE);
                        inputFrecuenciasMovimiento.setVisibility(View.GONE);
                    } else {
                        inputTiposMovimiento.setVisibility(View.VISIBLE);
                        inputFrecuenciasMovimiento.setVisibility(View.VISIBLE);

                        List<Domain> listDomainTemp = new LinkedList<>();
                        for(Domain row:listTiposMovimiento){
                            if(row.getValor()==null) {
                                listDomainTemp.add(row);
                            } else if(domain.getCodigo().equals("10")
                                    && Integer.valueOf(row.getCodigo()) >= 11 && Integer.valueOf(row.getCodigo()) <= 19) {
                                listDomainTemp.add(row);
                            } else if(domain.getCodigo().equals("20")
                                    && Integer.valueOf(row.getCodigo()) >= 21 && Integer.valueOf(row.getCodigo()) <= 24) {
                                listDomainTemp.add(row);
                            }
                        }
                        tiposMovimientoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listDomainTemp);
                        inputTiposMovimiento.setAdapter(tiposMovimientoAdapter);
                        inputTiposMovimiento.setSelection(positionTipoMovimiento);
                    }
                } else {
                    inputTiposMovimiento.setVisibility(View.GONE);
                    inputFrecuenciasMovimiento.setVisibility(View.GONE);
                }

                nextFlagClaseMovimiento = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        inputGrupoUso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                labelNombreCultivo.setVisibility(View.GONE);
                inputNombreCultivo.setVisibility(View.GONE);

                int positionSubgrupo = 0;
                if(nextFlagGrupoUso) {
                    clearGrupoUso();
                } else {
                    positionSubgrupo = inputSubgrupoUso.getSelectedItemPosition();
                }

                Domain domain = (Domain) inputGrupoUso.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (Integer.valueOf(domain.getCodigo()) >= 100) {
                        labelSubgrupoUso.setVisibility(View.GONE);
                        inputSubgrupoUso.setVisibility(View.GONE);
                    } else {
                        List<Domain> listDomainTemp = new LinkedList<>();
                        for (Domain row : listSubgrupoUso) {
                            if (row.getValor() == null) {
                                listDomainTemp.add(row);
                            } else if (domain.getCodigo().equals("10")
                                    && Integer.valueOf(row.getCodigo()) >= 11 && Integer.valueOf(row.getCodigo()) <= 13) {
                                listDomainTemp.add(row);
                            } else if (domain.getCodigo().equals("20")
                                    && Integer.valueOf(row.getCodigo()) >= 21 && Integer.valueOf(row.getCodigo()) <= 23) {
                                listDomainTemp.add(row);
                            } else if (domain.getCodigo().equals("30")
                                    && Integer.valueOf(row.getCodigo()) >= 31 && Integer.valueOf(row.getCodigo()) <= 32) {
                                listDomainTemp.add(row);
                            } else if (domain.getCodigo().equals("40")
                                    && Integer.valueOf(row.getCodigo()) >= 41 && Integer.valueOf(row.getCodigo()) <= 43) {
                                listDomainTemp.add(row);
                            } else if (domain.getCodigo().equals("50")
                                    && Integer.valueOf(row.getCodigo()) >= 51 && Integer.valueOf(row.getCodigo()) <= 52) {
                                listDomainTemp.add(row);
                            } else if (domain.getCodigo().equals("60")
                                    && Integer.valueOf(row.getCodigo()) >= 61 && Integer.valueOf(row.getCodigo()) <= 67) {
                                listDomainTemp.add(row);
                            } else if (domain.getCodigo().equals("70")
                                    && Integer.valueOf(row.getCodigo()) >= 71 && Integer.valueOf(row.getCodigo()) <= 73) {
                                listDomainTemp.add(row);
                            } else if (domain.getCodigo().equals("80")
                                    && Integer.valueOf(row.getCodigo()) >= 81 && Integer.valueOf(row.getCodigo()) <= 85) {
                                listDomainTemp.add(row);
                            } else if (domain.getCodigo().equals("90")
                                    && Integer.valueOf(row.getCodigo()) >= 91 && Integer.valueOf(row.getCodigo()) <= 98) {
                                listDomainTemp.add(row);
                            }
                        }
                        subgrupoUsoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listDomainTemp);
                        inputSubgrupoUso.setAdapter(subgrupoUsoAdapter);
                        inputSubgrupoUso.setSelection(positionSubgrupo);

                        labelSubgrupoUso.setVisibility(View.VISIBLE);
                        inputSubgrupoUso.setVisibility(View.VISIBLE);
                    }
                } else {
                    labelSubgrupoUso.setVisibility(View.GONE);
                    inputSubgrupoUso.setVisibility(View.GONE);
                    inputSubgrupoUso.setSelection(0);
                }

                nextFlagGrupoUso = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        inputSubgrupoUso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                String cultivo = null;
                if(nextFlagSubgrupoUso) {
                    clearSubgrupoUso();
                } else {
                    cultivo = inputNombreCultivo.getText().toString();
                }

                Domain domain = (Domain) inputSubgrupoUso.getSelectedItem();
                if(domain!=null && domain.getId()!=null) {
                    if (Integer.valueOf(domain.getCodigo()) == 11 ||
                            Integer.valueOf(domain.getCodigo()) == 12 ||
                            Integer.valueOf(domain.getCodigo()) == 13 ||
                            Integer.valueOf(domain.getCodigo()) == 31 ||
                            Integer.valueOf(domain.getCodigo()) == 32 ||
                            Integer.valueOf(domain.getCodigo()) == 41 ||
                            Integer.valueOf(domain.getCodigo()) == 42 ||
                            Integer.valueOf(domain.getCodigo()) == 43) {
                        labelNombreCultivo.setVisibility(View.VISIBLE);
                        inputNombreCultivo.setVisibility(View.VISIBLE);
                        inputNombreCultivo.setText(cultivo);
                    } else {
                        labelNombreCultivo.setVisibility(View.GONE);
                        inputNombreCultivo.setVisibility(View.GONE);
                        clearSubgrupoUso();
                    }
                } else {
                    labelNombreCultivo.setVisibility(View.GONE);
                    inputNombreCultivo.setVisibility(View.GONE);
                    clearSubgrupoUso();
                }

                nextFlagSubgrupoUso = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

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

                nextFlag = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        populateDomains();

        if(mode == null || mode == Modes.NEW) {
            inputNroObservacion.setText(dataBaseHelper.getNroObservacion("N"));
            inputFecha.setText(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            mode = Modes.NEW;
        } else {
            editForm();
        }

        return rootView;
    }

    public void onClick(View v) {

    }

    public void setFormNotaCampo(FormNotaCampo formNotaCampo) {
        this.formNotaCampo = formNotaCampo;
    }

    void populateDomains() {
        listReconocedor = dataBaseHelper.getListDomain("Y1 - RECONOCEDOR");
        listEpoca = dataBaseHelper.getListDomain("EPOCA");
        listGradiente = dataBaseHelper.getListDomain("X1 - GRADIENTE");
        listPendienteLongitud = dataBaseHelper.getListDomain("X2 - LONGITUD");
        listPendienteForma = dataBaseHelper.getListDomain("X3 - PENDIENTE FORMA");
        listClaseErosion = dataBaseHelper.getListDomain("G2 - CLASE DE EROSIÓN");
        listTipoErosion = dataBaseHelper.getListDomain("G3 - TIPO DE EROSIÓN");
        listGradoErosion = dataBaseHelper.getListDomain("G1 - GRADO DE EROSION");
        listClasesMovimiento = dataBaseHelper.getListDomain("G4 - CLASES DE MOVIMIENTOS EN MASA");
        listTiposMovimiento = dataBaseHelper.getListDomain("G5 - TIPOS  DE MOVIMIENTOS EN MASA");
        listFrecuenciasMovimiento = dataBaseHelper.getListDomain("G6 - FRECUENCIA DE MOVIMIENTOS EN MASA");
        listAnegamiento = dataBaseHelper.getListDomain("U2 - ANEGAMIENTO");
        listFrecuencia = dataBaseHelper.getListDomain("U3 - FRECUENCIA");
        listDuracion = dataBaseHelper.getListDomain("U4 - DURACION");
        listPedegrosidad = dataBaseHelper.getListDomain("S4 - PEDREGOSIDAD SUPERFICIAL");
        listAfloramiento = dataBaseHelper.getListDomain("S5 - AFLORAMIENTO ROCOSO");
        listGrupoUso = dataBaseHelper.getListDomain("W1 - GRUPOS DE USO DE LA TIERRA");
        listSubgrupoUso = dataBaseHelper.getListDomain("W2 - SUBGRUPOS DE USO DE LA TIERRA");

        reconocedorAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listReconocedor);
        inputReconocedor.setAdapter(reconocedorAdapter);

        epocaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listEpoca);
        inputEpoca.setAdapter(epocaAdapter);

        gradienteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listGradiente);
        inputGradiente.setAdapter(gradienteAdapter);

        pendienteLongitudAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listPendienteLongitud);
        inputPendienteLongitud.setAdapter(pendienteLongitudAdapter);

        pendienteFormaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listPendienteForma);
        inputPendienteForma.setAdapter(pendienteFormaAdapter);

        claseErosionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listClaseErosion);
        inputClaseErosion.setAdapter(claseErosionAdapter);

        tipoErosionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listTipoErosion);
        inputTipoErosion.setAdapter(tipoErosionAdapter);

        gradoErosionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listGradoErosion);
        inputGradoErosion.setAdapter(gradoErosionAdapter);

        clasesMovimientoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listClasesMovimiento);
        inputClasesMovimiento.setAdapter(clasesMovimientoAdapter);

        tiposMovimientoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listTiposMovimiento);
        inputTiposMovimiento.setAdapter(tiposMovimientoAdapter);

        frecuenciasMovimientoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listFrecuenciasMovimiento);
        inputFrecuenciasMovimiento.setAdapter(frecuenciasMovimientoAdapter);

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

        grupoUsoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listGrupoUso);
        inputGrupoUso.setAdapter(grupoUsoAdapter);

        subgrupoUsoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listSubgrupoUso);
        inputSubgrupoUso.setAdapter(subgrupoUsoAdapter);
    }

    public void saveAction(){
        nroObservacion = inputNroObservacion.getText().toString();
        reconocedor = ((Domain)inputReconocedor.getSelectedItem()).getId();
        nombreSitio = inputNombreSitio.getText().toString();
        fechaHora = inputFecha.getText().toString();
        epocaClimatica = ((Domain)inputEpoca.getSelectedItem()).getId();
        epocaDias = inputEpocaDias.getText().toString();
        gradiente = ((Domain)inputGradiente.getSelectedItem()).getId();
        pendienteLongitud = ((Domain)inputPendienteLongitud.getSelectedItem()).getId();
        pendienteForma = ((Domain)inputPendienteForma.getSelectedItem()).getId();
        tiposMovimiento = ((Domain)inputTiposMovimiento.getSelectedItem()).getId();
        anegamiento = ((Domain)inputAnegamiento.getSelectedItem()).getId();
        if(inputFrecuencia.getSelectedItem() == null){
            inputFrecuencia.setSelection(0);
        }
        frecuencia = ((Domain)inputFrecuencia.getSelectedItem()).getId();
        if(inputDuracion.getSelectedItem() == null){
            inputDuracion.setSelection(0);
        }
        duracion = ((Domain)inputDuracion.getSelectedItem()).getId();
        pedregosidad = ((Domain)inputPedegrosidad.getSelectedItem()).getId();
        afloramiento = ((Domain)inputAfloramiento.getSelectedItem()).getId();
        grupoUso = ((Domain) inputGrupoUso.getSelectedItem()).getId();
        subgrupoUso = ((Domain)inputSubgrupoUso.getSelectedItem()).getId();

        if(reconocedor == null || nombreSitio.isEmpty() || epocaClimatica == null ||
                pendienteLongitud == null || gradoErosion == null || tiposMovimiento == null || anegamiento == null ||
                pedregosidad == null  || afloramiento == null || grupoUso == null ||
                subgrupoUso == null ) {
            Toast.makeText(getActivity(), "Los campos marcados con * son obligatorios!", Toast.LENGTH_SHORT).show();
        } else {
            foto = ((BitmapDrawable)imgViewPic.getDrawable()).getBitmap();
            SaveFormTask task = new SaveFormTask(getActivity());
            task.execute();
        }
    }

    void clearClaseErosion() {
        inputTipoErosion.setSelection(0);
        inputGradoErosion.setSelection(0);
    }

    void clearClasesMovimiento() {
        inputTiposMovimiento.setSelection(0);
        inputFrecuenciasMovimiento.setSelection(0);
    }

    void clearGrupoUso() {
        inputSubgrupoUso.setSelection(0);
        clearSubgrupoUso();
    }

    void clearSubgrupoUso() {
        inputNombreCultivo.setText(null);
    }

    public void takePictureAction(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap pic = (Bitmap) extras.get("data");
        Bitmap tempPic = DBBitmapUtility.getResizedBitmap(pic, 60);
        imgViewPic.setImageBitmap(tempPic);
    }

    public void loadPictureAction(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn,
                null, null, null);
        String picturePath = null;
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        }
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

    public void editForm(){
        if(formNotaCampo == null){
            formNotaCampo = new FormNotaCampo();
            inputNroObservacion.setText(dataBaseHelper.getNroObservacion("C"));
            inputFecha.setText(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            mode = Modes.NEW;
        } else {
            inputNroObservacion.setText(formNotaCampo.getNroObservacion());
            inputFecha.setText(formNotaCampo.getFechaHora());
            int spinnerPosition = reconocedorAdapter.getPosition(Utils.getDomain(listReconocedor, formNotaCampo.getReconocedor()));
            inputReconocedor.setSelection(spinnerPosition);
            spinnerPosition = epocaAdapter.getPosition(Utils.getDomain(listEpoca, formNotaCampo.getEpocaClimatica()));
            inputNombreSitio.setText(formNotaCampo.getNombreSitio());
            inputEpoca.setSelection(spinnerPosition);
            inputEpocaDias.setText(formNotaCampo.getDiasLluvia());
            spinnerPosition = gradienteAdapter.getPosition(Utils.getDomain(listGradiente, formNotaCampo.getPendienteLongitud()));
            inputGradiente.setSelection(spinnerPosition);
            spinnerPosition = pendienteLongitudAdapter.getPosition(Utils.getDomain(listPendienteLongitud, formNotaCampo.getPendienteLongitud()));
            inputPendienteLongitud.setSelection(spinnerPosition);
            spinnerPosition = pendienteFormaAdapter.getPosition(Utils.getDomain(listPendienteForma, formNotaCampo.getGradoErosion()));
            inputPendienteForma.setSelection(spinnerPosition);
            spinnerPosition = clasesMovimientoAdapter.getPosition(Utils.getDomain(listClasesMovimiento, formNotaCampo.getTipoMovimiento()));
            inputClasesMovimiento.setSelection(spinnerPosition);
            spinnerPosition = anegamientoAdapter.getPosition(Utils.getDomain(listAnegamiento, formNotaCampo.getAnegamiento()));
            inputAnegamiento.setSelection(spinnerPosition);
            spinnerPosition = frecuenciaAdapter.getPosition(Utils.getDomain(listFrecuencia, formNotaCampo.getFrecuencia()));
            inputFrecuencia.setSelection(spinnerPosition);
            spinnerPosition = duracionAdapter.getPosition(Utils.getDomain(listDuracion, formNotaCampo.getDuracion()));
            inputDuracion.setSelection(spinnerPosition);
            spinnerPosition = pedegrosidadAdapter.getPosition(Utils.getDomain(listPedegrosidad, formNotaCampo.getPedregosidad()));
            inputPedegrosidad.setSelection(spinnerPosition);
            spinnerPosition = afloramientoAdapter.getPosition(Utils.getDomain(listAfloramiento, formNotaCampo.getAfloramiento()));
            inputAfloramiento.setSelection(spinnerPosition);
            spinnerPosition = grupoUsoAdapter.getPosition(Utils.getDomain(listGrupoUso, formNotaCampo.getGrupoUso()));
            inputGrupoUso.setSelection(spinnerPosition);
            spinnerPosition = subgrupoUsoAdapter.getPosition(Utils.getDomain(listSubgrupoUso, formNotaCampo.getSubgrupoUso()));
            inputSubgrupoUso.setSelection(spinnerPosition);

            formNotaCampoFoto = dataBaseHelper.getFormNotaCampoFoto(formNotaCampo.getId());
            Bitmap bitmap = DBBitmapUtility.loadImageBitmap(getActivity(), formNotaCampoFoto.getFoto());
            imgViewPic.setImageBitmap(bitmap);

            nextFlag = false;
            nextFlagClaseErosion = false;
            nextFlagClaseMovimiento = false;
            nextFlagGrupoUso = false;
            nextFlagSubgrupoUso = false;
        }
    }

    class SaveFormTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        public SaveFormTask(Context context) {
            progressDialog = new ProgressDialog(context);
        }

        public void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(getResources().getString(R.string.msg_loading));
            progressDialog.show();
        }

        public void onPostExecute(Void unused) {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Formulario " + formNotaCampo.getNroObservacion()
                    + " actualizado correctamente!", Toast.LENGTH_SHORT).show();
        }

        protected Void doInBackground(Void... params) {
            if(formNotaCampo == null || mode == Modes.NEW) {
                formNotaCampo = new FormNotaCampo();
                formNotaCampo.setLongitud(Singleton.getInstance().getX());
                formNotaCampo.setLatitud(Singleton.getInstance().getY());
                formNotaCampo.setFechaHora(fechaHora);
                formNotaCampo.setAltitud(null);
                formNotaCampo.setNroObservacion(nroObservacion);
                formNotaCampoFoto = new FormNotaCampoFoto();
            }

            formNotaCampo.setReconocedor(reconocedor);
            formNotaCampo.setNombreSitio(nombreSitio);
            formNotaCampo.setEpocaClimatica(epocaClimatica);
            formNotaCampo.setDiasLluvia(epocaDias);
            formNotaCampo.setPendienteLongitud(pendienteLongitud);
            formNotaCampo.setGradoErosion(gradoErosion);
            formNotaCampo.setTipoMovimiento(tiposMovimiento);
            formNotaCampo.setAnegamiento(anegamiento);
            formNotaCampo.setFrecuencia(frecuencia);
            formNotaCampo.setDuracion(duracion);
            formNotaCampo.setPedregosidad(pedregosidad);
            formNotaCampo.setAfloramiento(afloramiento);
            //formNotaCampo.setFragmentoSuelo(fragmentoSuelo);
            //formNotaCampo.setDrenajeNatural(drenajeNatural);
            //formNotaCampo.setProfundidadEfectiva(profundidadEfectiva);
            //formNotaCampo.setEpidedones(epidedones);
            //formNotaCampo.setEndopedones(endopedones);
            formNotaCampo.setEstado(1);

            DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
            Long result = dataBaseHelper.insertFormNotaCampo(formNotaCampo);
            if(result > 0) {
                formNotaCampoFoto.setIdFormNotaCampo(result.intValue());
                String nameFile = "img_" + nroObservacion + ".jpeg";
                DBBitmapUtility.saveImage(getActivity(), foto, nameFile);
                formNotaCampoFoto.setFoto(nameFile);
                dataBaseHelper.insertFormNotaCampoFoto(formNotaCampoFoto);
                mode = Modes.EDIT;
            }

            return null;
        }
    }

}
