package com.example.lightout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lightout.logic.Board;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFragment extends Fragment {

    private Button[][] btnArr;

    private Board board;
    public static final String boardBundleKey = "board_key";

    public BoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoardFragment newInstance(Board board) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();

        args.putSerializable(boardBundleKey, board);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            board = (Board) getArguments().getSerializable(boardBundleKey);
            Log.i("lightout-GameActivity", "board size: " + board.getSize());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_board, container, false);

        btnArr = new Button[board.getSize()][board.getSize()];

        TableLayout frag_container = (TableLayout) view.findViewById(R.id.table_board);
        TableRow tableRowArr[] = new TableRow[board.getSize()];
        for( int i=0; i < board.getSize(); ++i){
            tableRowArr[i]= new TableRow(getContext());
            TableRow tr = tableRowArr[i];
            frag_container.addView(tr);
            for( int j=0; j < board.getSize(); ++j){
                btnArr[i][j] = new Button(getContext());
                Button btn = btnArr[i][j];
                //TODO: set color
                tr.addView(btn);
            }
        }


        return view;
    }
}