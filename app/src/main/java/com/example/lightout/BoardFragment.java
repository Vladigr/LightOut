package com.example.lightout;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.lightout.boundary.TableButton;
import com.example.lightout.logic.Board;
import com.example.lightout.logic.Observer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFragment extends Fragment implements Observer {

    private TableButton[][] btnArr;
    private GameInterface gameInterface;

    private Board board;
    public static final String boardBundleKey = "board_key";
    private BoardListener listener;

    public interface BoardListener {
        void won();

        void onDestroy(Board board);
    }

    @Override
    public void onAttach(@NonNull Context context) {

        try {
            this.listener = (BoardListener) context;
            this.gameInterface=(GameInterface)context;
        } catch (ClassCastException e) {
            throw new ClassCastException("the class " +
                    context.getClass().getName() +
                    " must implements the interface 'WinListener'");
        }
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        if (board.checkWin() == false) {
            listener.onDestroy(board);
        }
        super.onDestroy();
    }

    public BoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param board Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
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
            board.attach(this);
            Log.i("BoardFragment.onCreate", "board size: " + board.getSize() + " board state[0][0]: " + board.getElementInBoard(0, 0));
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        btnArr = new TableButton[board.getSize()][board.getSize()];

        TableLayout frag_container = (TableLayout) view.findViewById(R.id.table_board);
        TableRow tableRowArr[] = new TableRow[board.getSize()];
        for (int i = 0; i < board.getSize(); ++i) {
            tableRowArr[i] = new TableRow(getContext());
            TableRow tr = tableRowArr[i];
            for (int j = 0; j < board.getSize(); ++j) {
                setButtonOnCreateView(i, j, tr);
            }
            frag_container.addView(tr);
        }
        return view;
    }

    private void setButtonOnCreateView(int i, int j, TableRow tr) {
        btnArr[i][j] = new TableButton(getContext(), i, j, board.getElementInBoard(i, j));
        TableButton btn = btnArr[i][j];
        //set Starting color
        int dpAsPixels = dpAsPixels(10);
        setColorForTableButton(i, j);

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TableButton tbtn = (TableButton) v;
                        board.makeMove(tbtn.getI(), tbtn.getJ());
                    }
                });
        tr.addView(btn);
    }

    private void setColorForTableButton(int i, int j) {
        TableButton btn = btnArr[i][j];
        if (board.getElementInBoard(i, j) == true) {
            btn.setBackgroundResource(R.drawable.on);
        } else {
            btn.setBackgroundResource(R.drawable.off);
        }
    }

    private int dpAsPixels(int sizeInDp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }

    @Override
    public void update(int i, int j, boolean val) {
        btnArr[i][j].setState(val);
        setColorForTableButton(i, j);
        Log.i("BoardFragment.update", "i,j,val: " + i + "," + j + "," + val);
    }

    @Override
    public void updateEnd() {
        Log.i("BoardFragment.updateEnd", "gameStatus: " + board.checkWin());
        if (board.checkWin()) {
            listener.won();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.board_game_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.solve:
                ArrayList<Board.SolPoints> solPoints = board.solve();
                Log.i("solve", solPoints.toString());
                for (Board.SolPoints sp : solPoints) {
                    btnArr[sp.getiScreen()][sp.getjScreen()].setBackgroundResource(R.drawable.solve);
                }
                Toast.makeText(getActivity(), "press the green button from bottom right to bottom left and to the top",Toast.LENGTH_LONG).show();
                break;
            //if the clicked button is Restart in the menu
            case R.id.btnRestart:
                gameInterface.restartGame();
                Log.i("elro","Restart game via menu");
                break;
            case R.id.btnExit:
                //TODO add saveing current state of the game and than function for terminating the app
                Log.i("elro","exit game via menu");

        }
        return super.onOptionsItemSelected(item);
    }

}