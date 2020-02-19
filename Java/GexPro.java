ems[i].swap_prices();
				//the_items[i].widen_margin();

				if (GeInteract.get_inv_cash() > the_items[i].buy_price) {
					check_error();
					String item_name = the_items[i].name_item;
					int quantity = (GeInteract.get_inv_cash()/ the_items[i].buy_price);
					if (quantity >= the_items[i].limit) {
						quantity = the_items[i].limit;
					}
					int buy_price = the_items[i].buy_price;
					int button = i;
					if(quantity > 0){
						GeInteract.make_buy_offer(button, item_name,quantity, buy_price, the_items[i].row);
						the_buttons[i].setBuy(i, quantity);
					}
				}
				
				if (Inventory.contains(the_items[i].item_id) || Inventory.contains((the_items[i].item_id) + 1)) {
					int quantity = GeInteract.count_num_items(the_items[i].item_id);
					int sell_price = the_items[i].sell_price;
					if (quantity > 0) {
						pause(300);
						if (Inventory.contains(the_items[i].item_id)) {	
							GeInteract.make_sell_offer(i + 3,the_items[i].item_id, quantity, sell_price);
						} else if (Inventory.contains(the_items[i].item_id + 1)) {
							GeInteract.make_sell_offer(i + 3,(the_items[i].item_id + 1), quantity, sell_price);
						}
						the_buttons[i + 3].setSell(i, quantity);
					}
				}
				calculate_total_cash();
				if(timer > 0){
					pause(2500);
				}	
			}
	
	public void new_adjust_buy_nex(boolean sell_hist_in[], int min_in, boolean did_offer, int the_index, double pct_change){
		if(did_offer){
			if(((!sell_hist_in[0])&&(!sell_hist_in[1])&&(!sell_hist_in[2])&&(!sell_hist_in[4])&&(!sell_hist_in[5]))){ 
				the_items[the_index].buy_price += pct_change*min_in;
				 //increase buying price
			}else if(sell_hist_in[0]){
				the_items[the_index].buy_price -= 1.6*pct_change*min_in;
				//decrease buying price
			}else{
				; //do nothing
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price += pct_change*min_in;
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 + min_in/2;
				the_items[the_index].buy_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 - min_in/2;
			}
		}
		//make sure that the margin is positive
	}
	public void new_adjust_buy(boolean sell_hist_in[], int min_in, boolean did_offer, int the_index, double pct_change){
		if(did_offer){
			if(((!sell_hist_in[0])&&(!sell_hist_in[1])&&(!sell_hist_in[2]))){ 
				the_items[the_index].buy_price += pct_change*min_in;
				 //increase buying price
			}else if(sell_hist_in[0]){
				the_items[the_index].buy_price -= 1.6*pct_change*min_in; 
				//decrease buying price
			}else{
				; //do nothing
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price += pct_change*min_in;
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 + min_in/2;
				the_items[the_index].buy_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 - min_in/2;
			}
		}
		//make sure that the margin is positive
	}
	public void new_adjust_sell_nex(boolean sell_hist_in[], int min_in, boolean did_offer, int the_index, double pct_change){
		if(did_offer){
			if(((!sell_hist_in[0])&&(!sell_hist_in[1])&&(!sell_hist_in[2])&&(!sell_hist_in[4])&&(!sell_hist_in[5]))){
				the_items[the_index].sell_price -= pct_change*min_in;
				 //decrease selling price
			}else if(sell_hist_in[0]){
				the_items[the_index].sell_price += 1.6*pct_change*min_in;
				//increase buying price
			}else{
				; //do nothing
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].buy_price -= pct_change*min_in;
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 + min_in/2;
				the_items[the_index].buy_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 - min_in/2;
			}
		}
		//make sure that the margin is positive
	}
	public void new_adjust_sell(boolean sell_hist_in[], int min_in, boolean did_offer, int the_index, double pct_change){
		if(did_offer){
			if((!sell_hist_in[0])&&(!sell_hist_in[1])&&(!sell_hist_in[2])){
				the_items[the_index].sell_price -= pct_change*min_in;
				 //decrease selling price
			}else if(sell_hist_in[0]){
				the_items[the_index].sell_price += 1.6*pct_change*min_in; 
				//increase buying price
			}else{
				; //do nothing
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].buy_price -= pct_change*min_in;
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 + min_in/2;
				the_items[the_index].buy_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 - min_in/2;
			}
		}
		//make sure that the margin is positive
	}
	public static boolean check_error(){
		
		//if grand exchange is not open, try opening it once
		if(GrandExchange.isOpen() == false){
			GeInteract.open_grand_exchange();
			pause(5000);
		}
		
		//if the grand exchange is not open
		if(GrandExchange.isOpen() == false){
			pause(2000);
			//make sure grand exchange or any interface is actually closed
			com.rsbuddy.script.methods.Environment.enableRandom("InterfaceCloser");
			pause(5000);
			//disable the script that logs us back in
			com.rsbuddy.script.methods.Environment.disableRandoms();
			//completely log out
			pause(5000);
			Game.logout(false);
			pause(5000);
			return true;
		}else{
			return false;
		}
	}
	
	public void adjust_sell_price(int offered_to_sell, int actually_sold, int the_index){
		double materiality = the_items[the_index].determine_materiality();
		double pct_adjust = ((4/offered_to_sell)*actually_sold - 2) * materiality;
		if(offered_to_sell >= the_items[the_index].limit && actually_sold == 0){
			pct_adjust *= 0.9;
		}else{
			pct_adjust *= 0.4;
		}
		the_items[the_index].sell_price *= (1 + pct_adjust);
		
	}
	
	public void adjust_buy_price(int offered_to_buy, int actually_bought, int the_index){
		double materiality = the_items[the_index].determine_materiality();
		double pct_adjust = -1 * ((4/offered_to_buy)*actually_bought - 2) * materiality;
		if(offered_to_buy >= the_items[the_index].limit && actually_bought == 0){
			pct_adjust *= 0.9;
		}else{
			pct_adjust *= 0.4;
		}
		the_items[the_index].buy_price *= (1 + pct_adjust);
	}
	
	public static void pause(int time_to_sleep) {
		int additional_pause = time_to_sleep/10;
		try {
			Thread.sleep(Random.nextInt(time_to_sleep + additional_pause, time_to_sleep));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void update_logout_time(){
		int hour = 5;
		int random_minute = Random.nextInt(10, 45);
		time_to_logout = current_time + (hour * 60) + random_minute;
	}
	
	public void six_hour_logout_rule(){
		if(time_to_logout < current_time){
			com.rsbuddy.script.methods.Environment.enableRandom("InterfaceCloser");
			pause(5000);
			com.rsbuddy.script.methods.Environment.disableRandoms();
			pause(5000);
			Game.logout(true);
			pause(15000);
			com.rsbuddy.script.methods.Environment.enableRandoms();		
			pause(15000);
			com.rsbuddy.script.methods.Environment.disableRandom("InterfaceCloser");
			update_logout_time();
			pause(5000);
		}
	}
	
	
}// end brackets

                                                                                                                                                                                                                                                                          , 69321);
			break;
		case 6:
			the_items[0] = new ItemClass("Ancient ceremonial mask", 2, 20125, "Special", 89312);
			the_items[1] = new ItemClass("Robin hood hat", 2, 2581, "Special", 59123);
			the_items[2] = new ItemClass("Saradomin armour set", 2, 11928, "Special", 34999);
			break;
		case 7:
			the_items[0] = new ItemClass("Armadyl chestplate", 2, 11720, "Special", 78777);
			the_items[1] = new ItemClass("Armadyl chainskirt", 2, 11722, "Special", 74133);
			the_items[2] = new ItemClass("Armadyl helmet", 2, 11718, "Special", 59311);
			break;
		case 8:
			the_items[0] = new ItemClass("Saradomin sword", 10, 11730);
			the_items[1] = new ItemClass("Dragon hatchet", 10, 6739);
			the_items[2] = new ItemClass("Staff of light", 10, 15486);	
			break;
		case 9:
			the_items[0] = new ItemClass("Armadyl Godsword", 2, 11694, "Special", 69321);
			the_items[1] = new ItemClass("Saradomin Godsword", 2, 11698, "Special", 69888);
			the_items[2] = new ItemClass("Zamorak Godsword", 2, 11700, "Special", 69323);
			break;
		case 10:
			
			break;
		case 11:	

		default:
			break;	
		}
	}

	/**
	 * THIS METHOD FINDS THE SELL PRICE:
	 * Check for errors
	 * Try buying the item starting at predefined price intervals until one buys
	 * Make our selling offer just below where we bought it.
	 * @param the_index
	 */
	public void get_sell_price(int the_index) {
		if (the_index >= 0 && the_index < num_of_items) {
			int count = 0;
			int ending_inventory_cash, beginning_inventory_cash;
			do {
				check_error();
				beginning_inventory_cash = GeInteract.get_inv_cash();
				GeInteract.make_buy_offer(0, the_items[the_index].name_item, 1, the_items[the_index].get_offer_buy_price(count));
				pause(1000);

				GeInteract.view_buy_offer(0);
				GeInteract.abort_buy_offer();
				pause(1500);

				ending_inventory_cash = GeInteract.get_inv_cash();
				count++;
			}while (beginning_inventory_cash == ending_inventory_cash);

			the_items[the_index].sell_price = beginning_inventory_cash - ending_inventory_cash - 3;
		}
	}
	/**
	 * THIS METHOD FINDS THE BUY PRICE:
	 * Check for errors
	 * Try selling the item starting at predefined price intervals until one sells
	 * Make our buying offer just above where we sold it.
	 * @param the_index
	 */
	
	public void get_buy_price(int the_index) {
		if (the_index >= 0 && the_index < num_of_items) {
			int count = 0;
			int beginning_inventory_cash, ending_inventory_cash;
			do {
				check_error();
				beginning_inventory_cash = GeInteract.get_inv_cash();
				GeInteract.make_sell_offer(0, the_items[the_index].item_id, 1, the_items[the_index].get_offer_sell_price(count));
				GeInteract.view_sell_offer(0);
				GeInteract.abort_sell_offer();
				pause(1500);
				ending_inventory_cash = GeInteract.get_inv_cash();
				count++;
			}while (beginning_inventory_cash == ending_inventory_cash);

			the_items[the_index].buy_price = ending_inventory_cash - beginning_inventory_cash + 3;
		}
	}
	
	/**
	 * THIS METHOD FLIPS TWO LIMIT ITEMS:
	 * Check for errors
	 * If there is a current offer in a given slot, abort the buy offer, and record how many we offered to buy and actually bought. 
	 * Store these values for a while, adjust buy price every couple of flip attempts, and reset.
	 * Repeat above for sell offers.
	 * Check to see that our minimum profit margin is met
	 * @param i The buy/sell index (0-2) 
	 * @param timer The number of times that the flip function was called.
	 */
	public void flipnex(int i, int timer) {
		if (timer == 0){
			the_items[i].initial_profit_margin = the_items[i].sell_price - the_items[i].buy_price;
			int the_middle = (the_items[i].sell_price + the_items[i].buy_price)/2;
			the_items[i].buy_price = the_middle - (int)0.4*the_items[i].initial_profit_margin;
			the_items[i].sell_price = the_middle + (int)0.4*the_items[i].initial_profit_margin;
		}
		


		if (the_buttons[i].isFree() == false) {
			check_error();
			GeInteract.view_buy_offer(i);
			GeInteract.abort_buy_offer();
			int num_items_offered_to_buy = the_buttons[i].num_items;
			the_items[i].offered_to_buy += num_items_offered_to_buy; 			//storing for future calculation
			int num_items_actually_bought = (GeInteract.count_num_items(the_items[i].item_id));
			the_items[i].actually_bought += num_items_actually_bought;			//storing for future calculation
			the_buttons[i].setEmpty();
		}
		
		if (((timer %1) == 0)&&(the_items[i].offered_to_buy != 0)) {
			boolean temp;
			
			if(the_items[i].actually_bought == 0){
				temp = false;
			}else{
				temp = true;
			}
			//adjust_buy_price(the_items[i].offered_to_buy, the_items[i].actually_bought, i, the_items[i].min_profit);
			boolean did_offer;
			if(the_items[i].offered_to_buy > 0){
				did_offer = true;
			}else{
				did_offer = false;
			}
			the_items[i].store_buy_hist(temp);
			new_adjust_buy_nex(the_items[i].buy_hist, the_items[i].min_profit, did_offer, i, .04);
			the_items[i].offered_to_buy = 0;
			the_items[i].actually_bought = 0;
		}

		if (the_buttons[i + 3].isFree() == false) {
			check_error();
			int num_items_just_purchased = GeInteract.count_num_items(the_items[i].item_id);
			int num_items_offered_to_sell = the_buttons[i+3].num_items;
			the_items[i].offered_to_sell += num_items_offered_to_sell;
			GeInteract.view_sell_offer(i + 3);
			GeInteract.abort_sell_offer();														
			int num_items_actually_sold = (num_items_just_purchased + num_items_offered_to_sell) - GeInteract.count_num_items(the_items[i].item_id);
			the_items[i].actually_sold += num_items_actually_sold;
			the_buttons[i+3].setEmpty();
		}
		
		if (((timer %1) == 0)&&(the_items[i].offered_to_sell != 0)) {
			boolean temp;
			boolean did_offer;
			if(the_items[i].actually_sold == 0){
				temp = false;
			}else{
				temp = true;
			}
			if(the_items[i].offered_to_sell > 0){
				did_offer = true;
			}else{
				did_offer = false;
			}
			the_items[i].store_sell_hist(temp);
			new_adjust_sell_nex(the_items[i].sell_hist, the_items[i].min_profit, did_offer, i, .04);
			//adjust_sell_price(the_items[i].offered_to_sell, the_items[i].actually_sold, i, the_items[i].min_profit);
			the_items[i].actually_sold = 0;
			the_items[i].offered_to_sell = 0;
			
		}
		
		//The min_profit is set to -1 when it has not been defined by the user
		if(the_items[i].min_profit < 0){
			the_items[i].widen_margin();
		}
		the_items[i].swap_prices();
	
		if (GeInteract.get_inv_cash() > the_items[i].buy_price) {
			check_error();
			String item_name = the_items[i].name_item;
			int quantity = (GeInteract.get_inv_cash()/ the_items[i].buy_price);
			if (quantity >= (the_items[i].limit)) {
				quantity = (the_items[i].limit);
			}
			
			int buy_price = the_items[i].buy_price;
			int button = i;

			if (quantity > 0) {
				//if we are buying 10, then the sell offer should be empty
				if((quantity + GeInteract.count_num_items(the_items[i].item_id)) >= the_items[i].limit){
					quantity = the_items[i].limit - GeInteract.count_num_items(the_items[i].item_id);
					if((the_items[i].item_type.equals("Regular"))&&(GeInteract.get_inv_cash() > the_items[i].buy_price*the_items[i].limit)){
						quantity = the_items[i].limit;			
					}
				}
				
				
				
				if(quantity > 0){
					GeInteract.make_buy_offer(button, item_name,quantity, buy_price);
					the_buttons[i].setBuy(i, quantity);
				}
				
				
			}
		}

		// if we have the item, put up a sell offer
		if (Inventory.contains(the_items[i].item_id) || Inventory.contains((the_items[i].item_id) + 1)) {
			
			// count items
			int quantity = GeInteract.count_num_items(the_items[i].item_id);
			int sell_price = the_items[i].sell_price;
			if (quantity > 0) {
				pause(500);//just added this pause
				if (Inventory.contains(the_items[i].item_id)) {	
					GeInteract.make_sell_offer(i + 3,the_items[i].item_id, quantity, sell_price);
				} else if (Inventory.contains(the_items[i].item_id + 1)) {
					GeInteract.make_sell_offer(i + 3,(the_items[i].item_id + 1), quantity, sell_price);
				}
				//log("Selling " + the_items[i].name_item + " Quantity:" + quantity + " Sell Price:" + sell_price);
				the_buttons[i + 3].setSell(i, quantity);

			}
		}
		
		calculate_total_cash();
		
		if(timer > 0){
			pause(10000);
		}
}
	public void flip2(int i, int timer) {
				if (timer == 0){
					the_items[i].initial_profit_margin = the_items[i].sell_price - the_items[i].buy_price;
				}
		
		
				if (the_buttons[i].isFree() == false) {
					check_error();
					GeInteract.view_buy_offer(i);
					GeInteract.abort_buy_offer();
					int num_items_offered_to_buy = the_buttons[i].num_items;
					the_items[i].offered_to_buy += num_items_offered_to_buy; 			//storing for future calculation
					int num_items_actually_bought = (GeInteract.count_num_items(the_items[i].item_id));
					the_items[i].actually_bought += num_items_actually_bought;			//storing for future calculation
					the_buttons[i].setEmpty();
				}
				
				if (((timer %1) == 0)&&(the_items[i].offered_to_buy != 0)) {
					boolean temp;
					
					if(the_items[i].actually_bought == 0){
						temp = false;
					}else{
						temp = true;
					}
					//adjust_buy_price(the_items[i].offered_to_buy, the_items[i].actually_bought, i, the_items[i].min_profit);
					boolean did_offer;
					if(the_items[i].offered_to_buy > 0){
						did_offer = true;
					}else{
						did_offer = false;
					}
					the_items[i].store_buy_hist(temp);
					new_adjust_buy(the_items[i].buy_hist, the_items[i].min_profit, did_offer, i, .09);
					the_items[i].offered_to_buy = 0;
					the_items[i].actually_bought = 0;
				}

				if (the_buttons[i + 3].isFree() == false) {
					check_error();
					int num_items_just_purchased = GeInteract.count_num_items(the_items[i].item_id);
					int num_items_offered_to_sell = the_buttons[i+3].num_items;
					the_items[i].offered_to_sell += num_items_offered_to_sell;
					GeInteract.view_sell_offer(i + 3);
					GeInteract.abort_sell_offer();														
					int num_items_actually_sold = (num_items_just_purchased + num_items_offered_to_sell) - GeInteract.count_num_items(the_items[i].item_id);
					the_items[i].actually_sold += num_items_actually_sold;
					the_buttons[i+3].setEmpty();
				}
				
				if (((timer %1) == 0)&&(the_items[i].offered_to_sell != 0)) {
					boolean temp;
					boolean did_offer;
					if(the_items[i].actually_sold == 0){
						temp = false;
					}else{
						temp = true;
					}
					if(the_items[i].offered_to_sell > 0){
						did_offer = true;
					}else{
						did_offer = false;
					}
					the_items[i].store_sell_hist(temp);
					new_adjust_sell(the_items[i].sell_hist, the_items[i].min_profit, did_offer, i, .09);
					//adjust_sell_price(the_items[i].offered_to_sell, the_items[i].actually_sold, i, the_items[i].min_profit);
					the_items[i].actually_sold = 0;
					the_items[i].offered_to_sell = 0;
					
				}
				
				//The min_profit is set to -1 when it has not been defined by the user
				if(the_items[i].min_profit < 0){
					the_items[i].widen_margin();
				}
				the_items[i].swap_prices();
			
				if (GeInteract.get_inv_cash() > the_items[i].buy_price) {
					check_error();
					String item_name = the_items[i].name_item;
					int quantity = (GeInteract.get_inv_cash()/ the_items[i].buy_price);
					if (quantity >= (the_items[i].limit)) {
						quantity = (the_items[i].limit);
					}
					
					int buy_price = the_items[i].buy_price;
					int button = i;

					if (quantity > 0) {
						//if we are buying 10, then the sell offer should be empty
						if((quantity + GeInteract.count_num_items(the_items[i].item_id)) >= the_items[i].limit){
							quantity = the_items[i].limit - GeInteract.count_num_items(the_items[i].item_id);
							if((the_items[i].item_type.equals("Regular"))&&(GeInteract.get_inv_cash() > the_items[i].buy_price*the_items[i].limit)){
								quantity = the_items[i].limit;			
							}
						}
						
						
						
						if(quantity > 0){
							GeInteract.make_buy_offer(button, item_name,quantity, buy_price);
							the_buttons[i].setBuy(i, quantity);
						}
						
						
					}
				}

				// if we have the item, put up a sell offer
				if (Inventory.contains(the_items[i].item_id) || Inventory.contains((the_items[i].item_id) + 1)) {
					
					// count items
					int quantity = GeInteract.count_num_items(the_items[i].item_id);
					int sell_price = the_items[i].sell_price;
					if (quantity > 0) {
						pause(500);//just added this pause
						if (Inventory.contains(the_items[i].item_id)) {	
							GeInteract.make_sell_offer(i + 3,the_items[i].item_id, quantity, sell_price);
						} else if (Inventory.contains(the_items[i].item_id + 1)) {
							GeInteract.make_sell_offer(i + 3,(the_items[i].item_id + 1), quantity, sell_price);
						}
						//log("Selling " + the_items[i].name_item + " Quantity:" + quantity + " Sell Price:" + sell_price);
						the_buttons[i + 3].setSell(i, quantity);

					}
				}
				
				calculate_total_cash();
				
				if(timer > 0){
					pause(10000);
				}
	}

	public void flip10(int i, int timer) {
		
		if (timer == 0){
			the_items[i].initial_profit_margin = the_items[i].sell_price - the_items[i].buy_price;
		}
				if (the_buttons[i].isFree() == false) {
					check_error();
					GeInteract.view_buy_offer(i);
					GeInteract.abort_buy_offer();
					int num_items_offered_to_buy = the_buttons[i].num_items;
					int num_items_actually_bought = (GeInteract.count_num_items(the_items[i].item_id));
					the_items[i].actually_bought = num_items_actually_bought;
					the_buttons[i].setEmpty();
					if (timer > 0) {
						adjust_buy_price(num_items_offered_to_buy, num_items_actually_bought, i);
					}
				}
				
				if (the_buttons[i + 3].isFree() == false) {
					check_error();
					int num_items_just_purchased = GeInteract.count_num_items(the_items[i].item_id);
					int num_items_offered_to_sell = the_buttons[i+3].num_items;
					GeInteract.view_sell_offer(i + 3);
					GeInteract.abort_sell_offer();														
					int num_items_actually_sold = (num_items_just_purchased + num_items_offered_to_sell) - GeInteract.count_num_items(the_items[i].item_id);
					the_items[i].actually_sold = num_items_actually_sold;
					the_buttons[i+3].setEmpty();
					if (timer > 0) {
						adjust_sell_price(num_items_offered_to_sell, num_items_actually_sold, i);
					}
				}
				
				the_items[i].swap_prices();
				the_items[i].widen_margin();

				if (GeInteract.get_inv_cash() > the_items[i].buy_price) {
					check_error();
					String item_name = the_items[i].name_item;
					int quantity = (GeInteract.get_inv_cash()/ the_items[i].buy_price);
					if (quantity >= the_items[i].limit) {
						quantity = the_items[i].limit;
					}
					int buy_price = the_items[i].buy_price;
					int button = i;

					if (quantity > 0) {
						if((quantity + GeInteract.count_num_items(the_items[i].item_id)) >= the_items[i].limit){
							quantity = the_items[i].limit - GeInteract.count_num_items(the_items[i].item_id);
						}
						if(quantity > 0){
							GeInteract.make_buy_offer(button, item_name,quantity, buy_price);
							the_buttons[i].setBuy(i, quantity);
						}
					}
				}
				
				if (Inventory.contains(the_items[i].item_id) || Inventory.contains((the_items[i].item_id) + 1)) {
					int quantity = GeInteract.count_num_items(the_items[i].item_id);
					int sell_price = the_items[i].sell_price;
					if (quantity > 0) {
						pause(500);
						if (Inventory.contains(the_items[i].item_id)) {	
							GeInteract.make_sell_offer(i + 3,the_items[i].item_id, quantity, sell_price);
						} else if (Inventory.contains(the_items[i].item_id + 1)) {
							GeInteract.make_sell_offer(i + 3,(the_items[i].item_id + 1), quantity, sell_price);
						}
						the_buttons[i + 3].setSell(i, quantity);
					}
				}
				calculate_total_cash();
				if(timer > 0){
					pause(10000);
				}	
			}
	public void new_adjust_buy_nex(boolean sell_hist_in[], int min_in, boolean did_offer, int the_index, double pct_change){
		if(did_offer){
			if(((!sell_hist_in[0])&&(!sell_hist_in[1])&&(!sell_hist_in[2])&&(!sell_hist_in[4])&&(!sell_hist_in[5]))){ 
				the_items[the_index].buy_price += pct_change*min_in;
				 //increase buying price
			}else if(sell_hist_in[0]){
				the_items[the_index].buy_price -= (0.04*(the_items[the_index].initial_profit_margin + the_items[the_index].min_profit)/2); 
				//decrease buying price
			}else{
				; //do nothing
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price += pct_change*min_in;
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 + min_in/2;
				the_items[the_index].buy_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 - min_in/2;
			}
		}
		//make sure that the margin is positive
	}
	public void new_adjust_buy(boolean sell_hist_in[], int min_in, boolean did_offer, int the_index, double pct_change){
		if(did_offer){
			if(((!sell_hist_in[0])&&(!sell_hist_in[1])&&(!sell_hist_in[2]))){ 
				the_items[the_index].buy_price += pct_change*min_in;
				 //increase buying price
			}else if(sell_hist_in[0]){
				the_items[the_index].buy_price -= (0.04*(the_items[the_index].initial_profit_margin + the_items[the_index].min_profit)/2); 
				//decrease buying price
			}else{
				; //do nothing
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price += pct_change*min_in;
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 + min_in/2;
				the_items[the_index].buy_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 - min_in/2;
			}
		}
		//make sure that the margin is positive
	}
	public void new_adjust_sell_nex(boolean sell_hist_in[], int min_in, boolean did_offer, int the_index, double pct_change){
		if(did_offer){
			if(((!sell_hist_in[0])&&(!sell_hist_in[1])&&(!sell_hist_in[2])&&(!sell_hist_in[4])&&(!sell_hist_in[5]))){
				the_items[the_index].sell_price -= pct_change*min_in;
				 //decrease selling price
			}else if(sell_hist_in[0]){
				the_items[the_index].sell_price += (0.04*(the_items[the_index].initial_profit_margin + the_items[the_index].min_profit)/2); 
				//increase buying price
			}else{
				; //do nothing
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].buy_price -= pct_change*min_in;
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 + min_in/2;
				the_items[the_index].buy_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 - min_in/2;
			}
		}
		//make sure that the margin is positive
	}
	public void new_adjust_sell(boolean sell_hist_in[], int min_in, boolean did_offer, int the_index, double pct_change){
		if(did_offer){
			if((!sell_hist_in[0])&&(!sell_hist_in[1])&&(!sell_hist_in[2])){
				the_items[the_index].sell_price -= pct_change*min_in;
				 //decrease selling price
			}else if(sell_hist_in[0]){
				the_items[the_index].sell_price += (0.04*(the_items[the_index].initial_profit_margin + the_items[the_index].min_profit)/2); 
				//increase buying price
			}else{
				; //do nothing
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].buy_price -= pct_change*min_in;
			}
			if(((the_items[the_index].sell_price)-(the_items[the_index].buy_price)) < min_in){
				the_items[the_index].sell_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 + min_in/2;
				the_items[the_index].buy_price = (the_items[the_index].buy_price + the_items[the_index].sell_price)/2 - min_in/2;
			}
		}
		//make sure that the margin is positive
	}
	public static boolean check_error(){
		
		//if grand exchange is not open, try opening it once
		if(GrandExchange.isOpen() == false){
			GeInteract.open_grand_exchange();
			pause(5000);
		}
		
		//if the grand exchange is not open
		if(GrandExchange.isOpen() == false){
			pause(2000);
			//make sure grand exchange or any interface is actually closed
			com.rsbuddy.script.methods.Environment.enableRandom("InterfaceCloser");
			pause(5000);
			//disable the script that logs us back in
			com.rsbuddy.script.methods.Environment.disableRandoms();
			//completely log out
			pause(5000);
			Game.logout(false);
			pause(5000);
			return true;
		}else{
			return false;
		}
	}
	
	public void adjust_sell_price(int offered_to_sell, int actually_sold, int the_index){
		double materiality = the_items[the_index].determine_materiality();
		double pct_adjust = ((4/offered_to_sell)*actually_sold - 2) * materiality;
		if(offered_to_sell >= the_items[the_index].limit && actually_sold == 0){
			pct_adjust *= 0.9;
		}else{
			pct_adjust *= 0.4;
		}
		the_items[the_index].sell_price *= (1 + pct_adjust);
		
	}
	
	public void adjust_buy_price(int offered_to_buy, int actually_bought, int the_index){
		double materiality = the_items[the_index].determine_materiality();
		double pct_adjust = -1 * ((4/offered_to_buy)*actually_bought - 2) * materiality;
		if(offered_to_buy >= the_items[the_index].limit && actually_bought == 0){
			pct_adjust *= 0.9;
		}else{
			pct_adjust *= 0.4;
		}
		the_items[the_index].buy_price *= (1 + pct_adjust);
	}
	
	public static void pause(int time_to_sleep) {
		int additional_pause = time_to_sleep/10;
		try {
			Thread.sleep(Random.nextInt(time_to_sleep + additional_pause, time_to_sleep));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void update_logout_time(){
		int hour = 5;
		int random_minute = Random.nextInt(10, 45);
		time_to_logout = current_time + (hour * 60) + random_minute;
	}
	
	public void six_hour_logout_rule(){
		if(time_to_logout < current_time){
			com.rsbuddy.script.methods.Environment.enableRandom("InterfaceCloser");
			pause(5000);
			com.rsbuddy.script.methods.Environment.disableRandoms();
			pause(5000);
			Game.logout(true);
			pause(15000);
			com.rsbuddy.script.methods.Environment.enableRandoms();		
			pause(15000);
			com.rsbuddy.script.methods.Environment.disableRandom("InterfaceCloser");
			update_logout_time();
			pause(5000);
		}
	}
	
	
}// end brackets

