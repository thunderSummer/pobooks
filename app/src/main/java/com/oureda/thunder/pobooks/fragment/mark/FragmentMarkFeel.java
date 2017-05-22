package com.oureda.thunder.pobooks.fragment.mark;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.Data.LabelFeel;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.person.NoteReadEditActivity;
import com.oureda.thunder.pobooks.base.MyApplication;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by thunder on 17-5-17.
 */

public class FragmentMarkFeel extends Fragment {
    @BindView(R.id.recycle_feel_read_note_person)
    RecyclerView recycleFeelReadNotePerson;
    Unbinder unbinder;
    private View view;
    private List<LabelFeel> labelFeelList;
    private BookNoteAdapter bookNoteAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_read_note_feel, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    class BookNoteAdapter extends RecyclerView.Adapter<BookNoteAdapter.ViewHolder>{
            private List<LabelFeel> labelFeelList;
            private View view;

            public BookNoteAdapter(List<LabelFeel> labelFeelList) {
                this.labelFeelList = labelFeelList;
            }

            private void share() {
            }

            public int getItemCount() {
                return this.labelFeelList.size();
            }

            public void onBindViewHolder(ViewHolder paramViewHolder, int paramInt) {
                LabelFeel localLabelFeel = (LabelFeel) this.labelFeelList.get(paramInt);

                paramViewHolder.bookAuthorFeelReadPerson.setText(DataSupport.where("BookId=?",localLabelFeel.getBookId()).find(Books.class).get(0).getAuthor());
              //  paramViewHolder.chapterLabelReadNote.setText("第" + localLabelFeel.getChapter() + "章");
                paramViewHolder.bookNameFeelReadPerson.setText(DataSupport.where("BookId=?",localLabelFeel.getBookId()).find(Books.class).get(0).getBookName());
                paramViewHolder.timeLabelReadNotePerson.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(localLabelFeel.getDate())));
            }

            public ViewHolder onCreateViewHolder(final ViewGroup paramViewGroup, int paramInt) {
                this.view = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_read_note_label, null);
                final ViewHolder viewHolder = new ViewHolder(this.view);
                viewHolder.deleteFeelReadPerson.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        int i = viewHolder.getAdapterPosition();
                        // DataSupport.deleteAll(LabelFeel.class, new String[]{"bookId = ? and chapter = ?", labelFeelList.get(i).getBookId(), String.valueOf(labelFeelList.get(i).getChapter())});
                        // Log.d("sdf", "onClick: " + ((LabelFeel) MarkReadNoteFragment.BookNoteAdapter.this.labelFeelList.get(i)).getBookId() + String.valueOf(paramAnonymousView.getChapter()));
                        LabelFeel labelFeel= labelFeelList.get(i);
                        labelFeel.setFeelContentAll("");
                        labelFeel.setLabelTime("暂无时间");
                        labelFeel.updateAll("bookId = ?",labelFeelList.get(i).getBookId(),String.valueOf(labelFeelList.get(i).getChapter()));
                        initAll();
                    }
                });
                viewHolder.editFeelReadPerson.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        int i = viewHolder.getAdapterPosition();
                        Intent intent = new Intent(MyApplication.getContext(), NoteReadEditActivity.class);
                        intent.putExtra("bookId", labelFeelList.get(i).getBookId());
                     //   intent.putExtra("chapter", labelFeelList.get(i).getChapter());
                        intent.putExtra("fact_edit", 2);
                        startActivity(intent);
                    }
                });
                viewHolder.shareFeelReadNotePerson.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        viewHolder.getAdapterPosition();
                        share();
                        ToastUtil.showToast("此功能即将上线，敬请期待");
                    }
                });
//            return paramViewGroup;
                return viewHolder;
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                @BindView(R.id.time_feel_read_note_person)
                TextView timeLabelReadNotePerson;
                @BindView(R.id.share_feel_read_note_person)
                ImageView shareFeelReadNotePerson;
                @BindView(R.id.book_name_feel_read_person)
                TextView bookNameFeelReadPerson;
                @BindView(R.id.book_author_feel_read_person)
                TextView bookAuthorFeelReadPerson;
                @BindView(R.id.delete_feel_read_person)
                ImageView deleteFeelReadPerson;
                @BindView(R.id.edit_feel_read_person)
                ImageView editFeelReadPerson;
                View view;

                public ViewHolder(View view) {
                    super(view);
                    this.view=view;
                    ButterKnife.bind(this, view);
                }
            }

    }
    private void initAll()
    {
        LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(MyApplication.getContext());
        initList();
        this.bookNoteAdapter = new BookNoteAdapter(labelFeelList);
        this.recycleFeelReadNotePerson.setLayoutManager(localLinearLayoutManager);
        this.recycleFeelReadNotePerson.setAdapter(this.bookNoteAdapter);
    }
    private void initList(){
        labelFeelList= DataSupport.where("feelContentAll != ? and chapter = ?","1").find(LabelFeel.class);
    }

}
