import com.rsbuddy.script.methods.GrandExchange;

@SuppressWarnings("deprecation")
public class ItemClass {
public int item_id;			//item id
public String name_item;	//for ge buying/selling
public int limit;					//ge buying limit (2-10,000); arbitrary ge restrictions
public int guide_price;
public double[] percentage_buy_intervals;
public double[] percentage_sell_intervals;
public String item_type;
public int min_profit;
public boolean[] sell_hist;
public boolean[] buy_hist;
public int initial_profit_margin;
public int[] buy_prices;
public int profit;
public int row;


//buy price
public int buy_price;
public int sell_price;
//the following are experimental variables for 2-limit items:
public int total_profit; //totaling profits for display
public int offered_to_buy;
public int actually_bought;
public int offered_to_sell;
public int actually_sold;


public void update_profit(int loop_num_in){
	if (loop_num_in == 0){
		profit = profit - initial_profit_margin;
	}
	if(actually_bought > 0){
		for (int x = 1; x <= actually_bought; x++){
			shift_back(buy_price);
		}
	}
	if(actually_sold > 0){
		for (int x = 1; x <= actually_sold; x++){
			int old_buy = shift_forward();
			if (old_buy == 0){
				old_buy = buy_price;
			}
			profit = profit + (sell_price - old_buy);
		}
	}
}

public void shift_back(int new_price){
	for(int x = 9; x > 0; x--){
		buy_prices[x] = buy_prices[x-1];
	}
	buy_prices[0] = new_price;
}
public int shift_forward(){
	int temp_ret = buy_prices[0];
	for(int x = 0; x < 9; x++){
		buy_prices[x] = buy_prices[x + 1];
	}
	buy_prices[9] = 0;
	return temp_ret;
}

public ItemClass(ItemClass other){
	buy_price = other.buy_price;
	guide_price = other.guide_price;
	item_id = other.item_id;
	limit = other.limit;
	buy_price = other.buy_price;
	sell_price = other.sell_price;
	total_profit = other.total_profit;
	offered_to_buy = other.offered_to_buy;
	actually_bought = other.actually_bought;
	offered_to_sell = other.offered_to_sell;
	actually_sold = other.actually_sold;
	item_type = other.item_type;
	min_profit = other.min_profit;
	sell_hist = other.sell_hist;
	buy_hist = other.buy_hist;
		sell_hist = other.sell_hist;
		buy_hist = other.sell_hist;	
	buy_prices = other.buy_prices;
	profit = other.profit;
	
	initialize_intervals();
}

public ItemClass(String name_item_in, int limit_in, int id_in, String item_type_in, int min_profit_in, int row_in){
	row = row_in;
	buy_price = 0;
	sell_price = 0;
	name_item = name_item_in;
	limit = limit_in;
	item_id = id_in;
	guide_price = GrandExchange.lookup(item_id).getGuidePrice();
	offered_to_buy = 0;
	actually_bought = 0;
	offered_to_sell = 0;
	min_profit = min_profit_in;
	item_type = item_type_in;
	
	profit = 0;
	buy_prices = new int[10];
	for(int x = 0; x < 10; x++){
		buy_prices[x] = 0;
	}
	
	sell_hist = new boolean[7]; 
	buy_hist = new boolean[7];
		sell_hist[0] = false;
		buy_hist[0] = false;
		sell_hist[1] = false;
		buy_hist[1] = false;
		sell_hist[2] = false;
		buy_hist[2] = false;
		sell_hist[3] = false;
		buy_hist[3] = false;
		sell_hist[4] = false;
		buy_hist[4] = false;
		sell_hist[5] = false;
		buy_hist[5] = false;
		sell_hist[6] = false;
		buy_hist[6] = false;
	
	
	
	actually_sold = 0;
	initialize_intervals();
}

public ItemClass(String name_item_in, int limit_in, int id_in, int row_in){
	row = row_in;
	buy_price = 0;
	sell_price = 0;
	name_item = name_item_in;
	limit = limit_in;
	item_id = id_in;
	guide_price = GrandExchange.lookup(item_id).getGuidePrice();
	offered_to_buy = 0;
	actually_bought = 0;
	offered_to_sell = 0;
	min_profit = -1;
	item_type = "";
	profit = 0;
	buy_prices = new int[10];
	for(int x = 0; x < 10; x++){
		buy_prices[x] = 0;
	}

	sell_hist = new boolean[7]; 
	buy_hist = new boolean[7];
	sell_hist[0] = false;
	buy_hist[0] = false;
	sell_hist[1] = false;
	buy_hist[1] = false;
	sell_hist[2] = false;
	buy_hist[2] = false;
	sell_hist[3] = false;
	buy_hist[3] = false;
	sell_hist[4] = false;
	buy_hist[4] = false;
	sell_hist[5] = false;
	buy_hist[5] = false;
	sell_hist[6] = false;
	buy_hist[6] = false;

	
	
	actually_sold = 0;
	initialize_intervals();
}

public void store_sell_hist(boolean new_in){
	for (int x = 5; x >= 0; x--){
		sell_hist[x + 1] = sell_hist[x];
	}
	sell_hist[0] = new_in;
	
}
public void store_buy_hist(boolean new_in){
	for (int x = 5; x >= 0; x--){
		buy_hist[x + 1] = buy_hist[x];
	}
	buy_hist[0] = new_in;
	
}

public void initialize_intervals(){
	percentage_buy_intervals = new double[6]; 
	percentage_buy_intervals[0] = 1.10;
	percentage_buy_intervals[1] = 1.15;
	percentage_buy_intervals[2] = 1.20;
	percentage_buy_intervals[3] = 1.30;
	percentage_buy_intervals[4] = 1.40;
	percentage_buy_intervals[5] = 1.50;

	percentage_sell_intervals = new double[6];
	percentage_sell_intervals[0] = 0.90;
	percentage_sell_intervals[1] = 0.85;
	percentage_sell_intervals[2] = 0.75;
	percentage_sell_intervals[3] = 0.65;
	percentage_sell_intervals[4] = 0.50;
	percentage_sell_intervals[5] = 0.25;
}

//start from beginning, move to end, then continue to add 1%
public int get_offer_buy_price(int count){
	if(count < percentage_buy_intervals.length){
		return (int)(guide_price * percentage_buy_intervals[count]);
	}else{
		int size_of_intervals = percentage_buy_intervals.length - 1;
		double temp_interval = percentage_buy_intervals[size_of_intervals];
		//for each count above the size of the intervals, try to add an additional percentage
		for(int i = 0; i < count - percentage_buy_intervals.length; i++){
			temp_interval += 0.04;
		}
		return (int)(temp_interval * guide_price);
	}
}

//start from end, move to beginning, then continue to subtract 1%
public int get_offer_sell_price(int count){
	if(count < percentage_sell_intervals.length){
		return (int)(guide_price * percentage_sell_intervals[count]);
	}else{
		int size_of_intervals = percentage_sell_intervals.length - 1;
		double temp_interval = percentage_sell_intervals[size_of_intervals];
		//for each count above the size of the intervals, try to add an additional percentage
		for(int i = 0; i < count - percentage_sell_intervals.length; i++){
			temp_interval -= 0.04;
		}
		return (int)(temp_interval * guide_price);
	}
}

public String toString(){
	return ("Name:" + name_item + " Buy Price:" + buy_price + " Sell Price:" + sell_price + " Margin:" + (sell_price - buy_price));
}

public double determine_materiality(){
	if(guide_price < 300000){
		return (0.01/6);
	}else if (guide_price < 40000000){
		return (1.01*Math.exp(-3.492*Math.pow(10,-8)*guide_price)/100/6);
	}else if (guide_price < 1000000000){
		return (.2775*Math.exp(-2.631*Math.pow(10,-9)*guide_price)/100/6);
	}else{
		return (0.0002/6);
	}
}

public void lower_selling_price(){
	sell_price *= (1 - determine_materiality());
}

public void lower_buying_price(){
	buy_price *= (1 - determine_materiality());
}

public void raise_selling_price(){
	sell_price *= (1 + determine_materiality());
}

public void raise_buying_price(){
	buy_price *= (1 + determine_materiality());
}

public void widen_margin(){
	int avg_price = (buy_price + sell_price)/2;
		buy_price = avg_price;
		sell_price = avg_price;
		for(int i = 0; i < 2; i++){
			lower_buying_price();
			raise_selling_price();
	}
}

public void swap_prices(){
	if(buy_price > sell_price){
		int temp = buy_price;
		buy_pr