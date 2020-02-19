import com.rsbuddy.script.methods.GrandExchange;

@SuppressWarnings("deprecation")
public class ItemClass {
public int item_id;			//item id
public String name_item;	//for ge buying/selling
public int limit;					//ge buying limit (2-10,000); arbitrary ge restrictions
public int guide_price;
public double[] percentage_buy_intervals;
public double[] percentage_sell_intervals;


//buy price
public int buy_price;
public int sell_price;

public ItemClass(ItemClass other){
	buy_price = other.buy_price;
	guide_price = other.guide_price;
	item_id = other.item_id;
	limit = other.limit;
	buy_price = other.buy_price;
	sell_price = other.sell_price;
	initialize_intervals();
}

public ItemClass(String name_item_in, int limit_in, int id_in){
	buy_price = 0;
	sell_price = 0;
	name_item = name_item_in;
	limit = limit_in;
	item_id = id_in;
	guide_price = GrandExchange.lookup(item_id).getGuidePrice();
	initialize_intervals();
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
	return ("Name:" + name_item + " ID:" + item_id + " Price:" + guide_price + " Buy Price:" + buy_price + " Sell Price:" + sell_price + " Margin:" + (sell_price - buy_price));
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

//num_items == 0
public void raise_range(){
	buy_price *= (1 + determine_materiality());
	sell_price *= (1 + determine_materiality());
}

//num_items == limit
public void lower_range(){
	buy_price *= (1 - determine_materiality());
	sell_price *= (1 - determine_materiality());
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
	double margin = (sell_price - buy_price)/((double)buy_price);
	int avg_price = (buy_price + sell_price)/2;
	if(margin < (determine_materiality()*1*avg_price)){
		buy_price = avg_price;
		sell_price = avg_price;
		for(int i = 0; i < 3; i++){
			lower_buying_price();
			raise_selling_price();
		}
		
	}
}

public void swap_prices(){
	if(buy_price > sell_price){
		int temp = buy_price;
		buy_price = sell_price;
		sell_price = temp;
	}
}




}




