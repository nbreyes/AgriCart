    package com.example.agricart;

    import android.content.Context;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.cardview.widget.CardView;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.ArrayList;

    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
        Context context;
        ArrayList<SearchStore> arrayList;

        public SearchAdapter(Context context, ArrayList<SearchStore> arrayList) {
            this.context = context;
            this.arrayList = arrayList;

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.search_cardview, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.searchStoreDisplay.setText(arrayList.get(position).getStoreName());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StoreClicked.class);
                    intent.putExtra("storeName", arrayList.get(position).getStoreName());
                    intent.putExtra("description", arrayList.get(position).getDescription());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView searchStoreDisplay;
            CardView cardView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                searchStoreDisplay = itemView.findViewById(R.id.searchStoreDisplay);
                cardView = itemView.findViewById(R.id.cardView);
            }
        }

    }
