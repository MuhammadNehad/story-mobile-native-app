package mainproject.mainroject.story;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by UNiversaL on 3/27/2018.
 */

public class viewpageradapter extends PagerAdapter{
Context context;
LayoutInflater inflater;
String[] sizes={"2","4","6","8","10","12","14","16","18","20","22","24","26","28","30"};
    private static final int Gallery_intent =1;


//    @Override
//    public Fragment getItem(int position) {
//        Bundle bundle = new Bundle();
//
//        return new pagesFragment();
//
//    }

    public viewpageradapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }
    EditText editText,storydesc;
    Button strycover, pdfupload;
    TextView filename;
    ImageButton alignleft;
    ImageButton alignright;
    ImageButton aligncenter;
    ListView textsize,textcolors;
    customadapter co;
 int[] colors ={R.color.AliceBlue,R.color.AntiqueWhite,R.color.Aqua,R.color.Aquamarine,R.color.Azure,R.color.Beige
                ,R.color.Bisque,R.color.Black,R.color.WhiteSmoke,R.color.YellowGreen,R.color.Violet,R.color.Turquoise
                ,R.color.Tomato,R.color.Thistle,R.color.Teal,R.color.Tan};
    customcoloradapter cca;

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        inflater =(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.pages,container,false);
        LinearLayout linearLayout= view.findViewById(R.id.pagenum);
        LinearLayout linearLayout2= view.findViewById(R.id.pdfform);
        editText = view.findViewById(R.id.userEntry);
        storydesc = view.findViewById(R.id.strydescription);
        pdfupload =(Button)view.findViewById(R.id.uploadpdffile);
        filename = (TextView)view.findViewById(R.id.uploadedfilename);
        alignleft = (ImageButton)view.findViewById(R.id.textalignleft);
        alignright = (ImageButton)view.findViewById(R.id.textalignright);
        aligncenter = (ImageButton)view.findViewById(R.id.textaligncenter);
        textsize =(ListView) view.findViewById(R.id.textsize);
        textcolors =(ListView)view.findViewById(R.id.textcolors);
        co= new customadapter(context,sizes);
        cca = new customcoloradapter();
// /
        ArrayAdapter sizesadapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,sizes);
//        sizesadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        ForegroundColorSpan olorSpan = new ForegroundColorSpan(
//                context.getResources()
//                        // Specify your color
//                        .getColor(R.color.your_font_color));
//        realPrice.setSpan(colorSpan,
//                0, // Start index of the single word
//                4, // End index of the single word
//                Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
     textcolors.setAdapter(cca);
        textsize.setAdapter(sizesadapter);
        textcolors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int startofselection = editText.getSelectionStart();
                int endofselection = editText.getSelectionEnd();

                String s = editText.getText().toString();
                final SpannableString editingbook = new SpannableString(s);
                SpannableStringBuilder sd= new SpannableStringBuilder();

                editingbook.setSpan(new ForegroundColorSpan(colors[position]), startofselection, endofselection, 0);


                editText.setText(editingbook);
                editText.clearComposingText();


            }
        });
        textsize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, sizes[position], Toast.LENGTH_LONG).show();

                int startofselection = editText.getSelectionStart();
                int endofselection = editText.getSelectionEnd();

                String s = editText.getText().toString();

                if(sizes[position] == "2") {
                    if ( endofselection > startofselection) {
                        final SpannableString editingbook = new SpannableString(s);
                        SpannableStringBuilder sd= new SpannableStringBuilder(s);
//                        sd.setSpan(new RelativeSizeSpan(1.1f), startofselection, endofselection, sd.SPAN_EXCLUSIVE_INCLUSIVE);
                        editingbook.setSpan(new RelativeSizeSpan(1.1f), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);
                        editingbook.setSpan(new ForegroundColorSpan(Color.RED), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);
//                        spannable.insert(12, "(& fon)");
                        editText.setText(editingbook);

                    }else
                        {Toast.makeText(context, "you Should select some character", Toast.LENGTH_LONG).show();
                    }
                }
                if(sizes[position] == "4"){
                    if(endofselection > startofselection) {
                        final SpannableString editingbook = new SpannableString(s);
                        SpannableStringBuilder sd= new SpannableStringBuilder();

                        editingbook.setSpan(new RelativeSizeSpan(1.2f), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);
                        editingbook.setSpan(new ForegroundColorSpan(Color.RED), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);

                        editText.setText(editingbook);
                    }else
                        {Toast.makeText(context, "you Should select some character", Toast.LENGTH_LONG).show();
                    }}
                if(sizes[position] == "6"){
//                    if(endofselection > startofselection) {
                        final SpannableString editingbook = new SpannableString(s);
                        SpannableStringBuilder sd= new SpannableStringBuilder();

                        editingbook.setSpan(new RelativeSizeSpan(1.4f), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);
                        editingbook.setSpan(new ForegroundColorSpan(Color.RED), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);

                        editText.setText(editingbook);
//                    }else
//                        {Toast.makeText(context, "you Should select some character", Toast.LENGTH_LONG).show();
//                    }
                }if(sizes[position] == "8"){
//                    if(endofselection > startofselection) {
                        final SpannableString editingbook = new SpannableString(s);
                        SpannableStringBuilder sd= new SpannableStringBuilder();

                        editingbook.setSpan(new RelativeSizeSpan(1.6f), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);
                        editingbook.setSpan(new ForegroundColorSpan(Color.RED), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);
//                    string.setSpan(new UnderlineSpan(), 10, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    string.setSpan(new RelativeSizeSpan(1.5f), 10, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    string.setSpan(new BackgroundColorSpan(color), 12, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    TextUtils.concat(editingbook)
                    editText.setText(editingbook);
//                    }else{Toast.makeText(context, "you Should select some character", Toast.LENGTH_LONG).show();
//                    }
                }if(sizes[position] == "10"){
//                    if(startofselection != 0 && endofselection !=0) {
                        final SpannableString editingbook = new SpannableString(s);
                        SpannableStringBuilder sd= new SpannableStringBuilder();

                        editingbook.setSpan(new RelativeSizeSpan(1.8f), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);
                        editingbook.setSpan(new ForegroundColorSpan(Color.RED), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);
                        editText.setText(editingbook);
//                }else{Toast.makeText(context, "you Should select some character", Toast.LENGTH_LONG).show();
//                }
//                    }
                }if(sizes[position] == "12"){
//                    if(endofselection > startofselection) {
                        final SpannableString editingbook = new SpannableString(s);
                        SpannableStringBuilder sd= new SpannableStringBuilder();

                        editingbook.setSpan(new RelativeSizeSpan(2.0f), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);
                        editingbook.setSpan(new ForegroundColorSpan(Color.RED), startofselection, endofselection, editingbook.SPAN_EXCLUSIVE_INCLUSIVE);

                        editText.setText(editingbook);
//                    }else{Toast.makeText(context, "you Should select some character", Toast.LENGTH_LONG).show();
//                    }
                }
                editText.clearComposingText();

            }

        });


        Bundle getsavedtext = new Bundle();
        if(getsavedtext == null){editText.setHint("Type Your Story Here");}
        else {
            getsavedtext.getString("contentfromapp");
        }
        alignleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    editText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    editText.clearComposingText();

                }
            }
        });
        aligncenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    editText.clearComposingText();

                }
            }
        });
        alignright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    editText.setSelection(View.TEXT_ALIGNMENT_TEXT_START);
                    editText.clearComposingText();

                }
            }
        });

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(final ViewGroup container, int position, final Object object) {
//     container.removeView((LinearLayout)object);
//if(container==null) {
//}else if(container!=null){
//
//    for (int i=0; i<= getCount(); i++ ){
//
//        SharedPreferences
                container.removeView((LinearLayout)object);
container.setSaveEnabled(true);
//    }
}
//((ViewPager) container).removeView((View) object);

    public void destroyItem(View container, int position, Object object) {
        throw new UnsupportedOperationException("Required method destroyItem was not overridden");
        }

    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

if(editText.getText().toString() != null || editText.getText().toString()!= " " ) {
    outState.putString("ourKey", editText.getText().toString());
    editText.onSaveInstanceState();
}   }
    public class customadapter extends ArrayAdapter<String> {
        Context cx;
        String[] textsizeselector;


        public customadapter(@NonNull Context context, String[] textsizeselectors) {
            super(context, R.layout.textsizecont);
            this.cx = context;
            this.textsizeselector = textsizeselectors;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row= inflater.inflate(R.layout.textsizecont,null);
            TextView txt = (TextView)row.findViewById(R.id.textsze);
            txt.setText(textsizeselector[position]);

            return row;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row= inflater.inflate(R.layout.textsizecont,null);
            TextView txt = (TextView)row.findViewById(R.id.textsze);
            txt.setText(textsizeselector[position]);
            return row;
        }
    }
    public class customcoloradapter extends BaseAdapter {
        Context cx;
        int[] textcolorselector;

//
//        public customcoloradapter(@NonNull Context context, int[] textcolorselectors) {
//            this.cx = context;
//            this.textcolorselector= textcolorselectors;
//        }

//        @Override
//        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//            LayoutInflater inflater = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View row= inflater.inflate(R.layout.textsizecont,null);
//            ImageButton txt = (ImageButton) row.findViewById(R.id.colorbtn);
//            txt.setImageResource(textcolorselector[position]);
//
//            return row;
//        }

        @Override
        public int getCount() {
            return colors.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.textsizecont,null);
            ImageButton txt = (ImageButton) convertView.findViewById(R.id.colorbtn);
            txt.setImageResource(colors[position]);
            return convertView;
        }
    }
}
