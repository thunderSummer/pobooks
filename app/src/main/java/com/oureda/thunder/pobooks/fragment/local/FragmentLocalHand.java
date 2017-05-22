package com.oureda.thunder.pobooks.fragment.local;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.oureda.thunder.pobooks.Data.LocalBook;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.MyApplication;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by thunder on 17-5-14.
 */

public class FragmentLocalHand extends Fragment {
    private String TAG = "FragmentLocalHand";
    @BindView(R.id.show_dir_local_fragment)
    TextView showDirLocalFragment;
    @BindView(R.id.back_dir_local_fragment)
    TextView backDirLocalFragment;
    @BindView(R.id.list_view_dir)
    RecyclerView listViewDir;
    Unbinder unbinder;
    private Context context;
    private View view;
    private File[] files;
    private ArrayList<File> fileArrayList;
    private LinkedList<String> pathStack;
    private String rootPath;
    private FileAdapter fileAdapter;
    private OnBackListener onBacklistener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_local_hand, null);
        unbinder = ButterKnife.bind(this, view);
        initAll();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void addFile(File[] files) {
        if (files != null) {
            int length = files.length;
            int j = 0;
            while (j < length) {
                File localFile = files[j];
                if (localFile.isDirectory()) {
                    this.fileArrayList.add(localFile);
                    j++;
                    continue;
                } else {
                    String path = files[j].getAbsolutePath();
                    String nameAll = "";
                    nameAll = path.substring(path.lastIndexOf("/") + 1);
                    if (!nameAll.isEmpty()) {
                        if (nameAll.startsWith(".")) {
                            j++;
                            continue;
                        } else {
                            if (nameAll.endsWith(".txt") || nameAll.endsWith(".umd") || nameAll.endsWith(".ekb2")) {
                                fileArrayList.add(localFile);
                                j++;
                            }
                        }
                    }
                    j++;
                }
            }
        }

    }

    private String getCurrentPath() {
        LinkedList localLinkedList = new LinkedList();
        localLinkedList.addAll(this.pathStack);
        String result = "";
        while (localLinkedList.size() != 0) {
            result = localLinkedList.pop() + result;
        }
        LogUtil.d(TAG, "getCurrentPath: " + result);
        return result;
    }

    private void initAll() {
        backDirLocalFragment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                back(1);
            }
        });
        context = MyApplication.getContext();
        this.fileArrayList = new ArrayList();
        this.rootPath = Environment.getExternalStorageDirectory().toString();
        this.pathStack = new LinkedList();
        this.pathStack.push(this.rootPath);
        this.files = Environment.getExternalStorageDirectory().listFiles();
        addFile(files);
        showDirLocalFragment.setText(getCurrentPath());
        this.fileAdapter = new FileAdapter(this.fileArrayList);
        initRecycle();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initRecycle(){
        listViewDir.setLayoutManager(new LinearLayoutManager(context));
        listViewDir.setAdapter(fileAdapter);
    }

    public void back(int type) {
            if(pathStack.size()>1){
                pathStack.pop();
                showPathChange();
            }else{
                if(type==1){
                    ToastUtil.showToast("已经到顶了");
                }else{
                    onBacklistener.finish();
                }

            }
    }
    private void showPathChange()
    {
        files = new File(getCurrentPath()).listFiles();
        if(files!=null){
            LogUtil.d(TAG, "" + this.files.length);
            this.fileArrayList.clear();
            addFile(this.files);
            this.fileAdapter = new FileAdapter(this.fileArrayList);
            initRecycle();
            showDirLocalFragment.setText(getCurrentPath());
        }

    }
    public  interface OnBackListener
    {
         void finish();
    }
    public void setOnBackListener(OnBackListener paramOnBackListener)
    {
        this.onBacklistener = paramOnBackListener;
    }

     class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
        private View view;
        private ArrayList<File> fileList;

        public FileAdapter(ArrayList<File> fileList) {
            this.fileList = fileList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local_hand, null);
            final ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    File file = fileArrayList.get(position);
                    if (file.isDirectory()) {
                        pathStack.push("/" + file.getName());
                        showPathChange();
                    }
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            File file = fileArrayList.get(position);
            String path = file.getAbsolutePath();
            if (file.isDirectory()) {
                holder.checkDir.setVisibility(View.GONE);
                File[] files = file.listFiles();
                int count = 0;
                for (File file1 : files) {
                    if (file.isDirectory()) {
                        count++;
                    } else {
                        String path1 = file1.getPath();
                        String name = path.substring(path.lastIndexOf("/") + 1);
                        if (name.startsWith(".")) {

                        } else {
                            if (name.endsWith(".txt") || name.endsWith(".ekb2") || name.endsWith(".umd")) {
                                count++;
                            }
                        }
                    }
                }
                holder.sizeCountDir.setText("总共" + count + "项");
                holder.styleLocalDir.setImageResource(R.drawable.file_icon);
            }else {
                holder.checkDir.setVisibility(View.VISIBLE);
                if(path.endsWith(".tex")){
                    holder.styleLocalDir.setImageResource(R.drawable.txt_icon);
                }else if(path.endsWith(".umd")){
                    holder.styleLocalDir.setImageResource(R.drawable.umd_icon);
                }else if(path.endsWith(".ekb2")){
                    holder.styleLocalDir.setImageResource(R.drawable.ekb2_icon);
                }
                LocalBook localBook=new LocalBook();
                localBook.setSize(file.length());
                holder.sizeCountDir.setText(localBook.getHumanSize());
            }
            holder.titleDir.setText(path.substring(path.lastIndexOf("/")+1));
            holder.dateDir.setText(new SimpleDateFormat("yyyy-MM-dd").format(file.lastModified()));


        }

        @Override
        public int getItemCount() {
            return fileList.size();
        }

         class ViewHolder  extends RecyclerView.ViewHolder{
            @BindView(R.id.style_local_dir)
            ImageView styleLocalDir;
            @BindView(R.id.title_dir)
            TextView titleDir;
            @BindView(R.id.size_count_dir)
            TextView sizeCountDir;
            @BindView(R.id.date_dir)
            TextView dateDir;
            @BindView(R.id.check_dir)
            CheckBox checkDir;
             View view;
            ViewHolder(View view) {
                super(view);
                this.view=view;
                ButterKnife.bind(this, view);
            }
        }
    }

}
