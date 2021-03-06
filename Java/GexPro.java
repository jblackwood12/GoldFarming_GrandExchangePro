import java.awt.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

import javax.rmi.CORBA.Util;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.Manifest;
import com.rsbuddy.script.methods.Camera;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.methods.Mouse;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Players;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Item;
import com.rsbuddy.script.wrappers.Npc;
import org.rsbuddy.widgets.GrandExchange;
import org.rsbuddy.widgets.Bank;

@Manifest(authors = { "jblackwood" }, name = "GexPro", description = "Flips items for a profit!", version = 1.0)
public class GexPro extends ActiveScript implements PaintListener {
	public long start_time = 0;
	public long current_time = 0;
	public int beginning_cash = 0;
	public int ending_cash = 0;
	public int cashGained = 0;
	
	public int withdraw_cash = 1000000000;
	
	public int num_of_items = 3;
	public int number_of_list;

	public ItemClass[] the_items;
	public OfferButton[] the_buttons;

    private final Color color1 = new Color(0, 0, 0);
    private final Color color2 = new Color(255, 255, 255);
	private final RenderingHints anialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	private final BasicStroke stroke1 = new BasicStroke(1);
	private final Font font1 = new Font("Arial", 0, 12);
	private final Font font2 = new Font("Arial", 0, 18);
	private final Image img1 = getImage("http://pictat.com/i/2011/5/31/21061goldbarsli.jpg");
	
	private Image getImage(String url) {
	        try {
	            return ImageIO.read(new URL(url));
	        } catch(IOException e) {
	            return null;
	        }
	    }
	
	public void onRepaint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		
		g.setRenderingHints(anialiasing);
		g.setColor(color1);
		g.setStroke(stroke1); 
		
		long millis = System.currentTimeMillis() - start_time;
		long hours = millis / (1000 * 60 * 60);
		millis -= hours * (1000 * 60 * 60);
		long minutes = millis / (1000 * 60);
		String mins = "" + minutes;
		while (mins.length() < 2) {
			mins = "0" + mins;
		}
		millis -= minutes * (1000 * 60);
		long seconds = millis / 1000;
		String secs = "" + seconds;
		while (secs.length() < 2) {
			secs = "0" + secs;
		}
		float xpsec = 0;
		if ((minutes > 0 || hours > 0 || seconds > 0) && cashGained != 0){
			xpsec = ((float) cashGained) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));
		}
		float xpmin = xpsec * 60;
		float xphour = xpmin * 60;

        g.setColor(color1);
        g.drawImage(img1, 539, 273, null);
        g.setFont(font2);
        g.drawString("GexPro", 560, 313);
        g.drawString("By JBlackwood", 565, 328);
        g.setFont(font1);
        g.drawString("Total Profit:" + cashGained, 550, 373);
        g.drawString("Time Ran:" + hours + ":" + mins + ":" + secs, 550, 358);
        g.drawString("GP/HOUR:" + (int)xphour, 550, 387);
        g.drawString("Starting GP: " + beginning_cash, 550, 400);
        g.drawString("Current GP: " + ending_cash , 550, 413);
        
	}
	
	
	
	public boolean onStart() {
		//start the timer, used to measure how long the program has run and is used for exiting the program
		start_time = System.currentTimeMillis();
		
		if (!Game.isLoggedIn()) {
			log("Start script logged in");
			return false;
		}

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {

					// get the number of items
					do {
						String temp0 = JOptionPane.showInputDialog(null,
								"Enter in the number of the list to flip from (1-10)",
								"Type List Number", JOptionPane.QUESTION_MESSAGE);
						try {
							//store the number of the list chosen
							number_of_list = Integer.parseInt(temp0);

						} catch (NumberFormatException ignored) {
						}
					} while (num_of_items < 0 && num_of_items > 10);
				}
			});
		} catch (Exception ignored) {
		}

		return true;
	}

	public int loop() {
		com.rsbuddy.script.methods.Environment.disableRandom("InterfaceCloser");
		
		//create button objects
		instantiate_buttons();
		
		//create the items based on what list was chosen
		instantiate_items();

		beginning_cash = GeInteract.get_inv_cash();
		GeInteract.open_grand_exchange();
		//MAKE SURE GRAND EXCHANGE IS OPEN (make sure it is open after we try opening it for the first time)
		check_error();
		for (int i = 0; i < num_of_items; i++) {
			get_sell_price(i);
			get_buy_price(i);
			// if buy price is greater than sell price, swap the prices
			the_items[i].swap_prices();
		}
		
		flip();
		
		//after we are done flipping items, log out completely
		check_error();
		
		return -1;
	}

	public void calculate_total_cash(){
		//update the time
		current_time = System.currentTimeMillis();
		
		//calculate the total cash
		
		//count amount of cash in inventory
		ending_cash = GeInteract.get_inv_cash();
		
		//count num of items in buy offers
		for(int j = 0; j < 3; j++){
			//if the button has an item in it
			if(the_buttons[j].isFree() == false){
				ending_cash += the_buttons[j].num_items * the_items[j].buy_price;
			}
		}
		
		//count num of items in sell offers
		for(int j = 0; j < 3; j++){
			if(the_buttons[j+3].isFree() == false){
				ending_cash += the_buttons[j+3].num_items * the_items[j].buy_price;
			}
		}
		
		cashGained = ending_cash - beginning_cash;
		//TODO count number of items in inventory
		
		
	}
	
	public void instantiate_buttons() {
		the_buttons = new OfferButton[6];
		for (int i = 0; i < 6; i++) {
			the_buttons[i] = new OfferButton();
		}
	}
	
	public void instantiate_items(){
		the_items = new ItemClass[num_of_items];
		
		//all of the lists are hardcoded here
		switch(number_of_list){
		case 1:
			the_items[0] = new ItemClass("Bandos godsword", 10, 11696);				
			the_items[1] = new ItemClass("Dragonfire shield", 10, 11284); 	
			the_items[2] = new ItemClass("Amulet of fury", 10, 6585); 		
			break;
		case 2:
			the_items[0] = new ItemClass("Dragon pickaxe", 10, 15259);
			the_items[1] = new ItemClass("Barrows - dharok's set", 10, 11848);
			the_items[2] = new ItemClass("Dragon claws", 10, 14484);
			break;
		case 3:
			the_items[0] = new ItemClass("Barrows - guthan's set", 10, 11850);	//3m	
			the_items[1] = new ItemClass("Zamorak godsword", 10, 11700);	//10m
			the_items[2] = new ItemClass("Bandos tassets", 10, 11726);		//18m
			break;
		case 4:	
			the_items[0] = new ItemClass("Bandos chestplate", 10, 11724);
			the_items[1] = new ItemClass("Barrows - verac's set", 10, 11856);
			the_items[2] = new ItemClass("Master wand", 10, 6914);
			
			break;
		case 5:
			break;
		case 6:

			break;
		case 7:
			break;
		case 8:
			break;
		case 9:
			break;
		case 10:
			break;
		default:
			break;	
		}
	}

	public void get_sell_price(int the_index) {
		if (the_index >= 0 && the_index < num_of_items) {
			int count = 0;
			int ending_inventory_cash = 0;
			int beginning_inventory_cash = 0;
			while (beginning_inventory_cash == ending_inventory_cash) {
				//MAKE SURE GRAND EXCHANGE IS OPEN (make sure it is open before making a buy offer)
				check_error();
				beginning_inventory_cash = GeInteract.get_inv_cash();
				GeInteract.make_buy_offer(0, the_items[the_index].name_item, 1, the_items[the_index].get_offer_buy_price(count));
				pause(1000);

				GeInteract.view_buy_offer(0);
				GeInteract.abort_buy_offer();
				pause(1500);

				ending_inventory_cash = GeInteract.get_inv_cash();
				count++;
			}

			the_items[the_index].sell_price = beginning_inventory_cash - ending_inventory_cash - 3;
		}
	}

	public void get_buy_price(int the_index) {
		if (the_index >= 0 && the_index < num_of_items) {
			int count = 0;
			int ending_inventory_cash = 0;
			int beginning_inventory_cash = 0;
			while (beginning_inventory_cash == ending_inventory_cash) {
				//MAKE SURE GRAND EXCHANGE IS OPEN (make sure it is open before making a sell offer)
				check_error();
				beginning_inventory_cash = GeInteract.get_inv_cash();
				GeInteract.make_sell_offer(0, the_items[the_index].item_id, 1,
						the_items[the_index].get_offer_sell_price(count));

				GeInteract.view_sell_offer(0);
				GeInteract.abort_sell_offer();
				pause(1500);
				ending_inventory_cash = GeInteract.get_inv_cash();
				count++;
			}

			//log("Sold item for:" + (ending_inventory_cash - beginning_inventory_cash));
			the_items[the_index].buy_price = ending_inventory_cash
					- beginning_inventory_cash + 3;
			//log(the_items[the_index].toString());
		}
	}

	public void flip() {
		int timer = 7200000;
		while (timer > 0) {
			// FLIP HERE

			// for each item, cancel the buy/sell offers, put up new offers
			for (int i = 0; i < the_items.length; i++) {
				
				// cancel buy offers / adjust buy offer price / put up new buy offers if we have enough money
				if (the_buttons[i].isFree() == false) {
					//make sure grand exchange page is open
					check_error();
					
					// counts number of items owned, stores in num_items_owned
					GeInteract.view_buy_offer(i);
					GeInteract.abort_buy_offer();
					// #of items we offered to buy
					int num_items_offered_to_buy = the_buttons[i].num_items;
					//log("num_items_offered_to_buy:" + num_items_offered_to_buy);
					// #of items we actually bought
					int num_items_actually_bought = (GeInteract.count_num_items(the_items[i].item_id));
					//log("num_items_actually_bought:" + num_items_actually_bought);
					the_buttons[i].setEmpty();

					// adjust buying prices
					// only run after we cycle through once
					if (timer != 7200000) {
						adjust_buy_price(num_items_offered_to_buy, num_items_actually_bought, i);
					}
				}

				// cancel sell offers / adjust sell offer price 
				if (the_buttons[i + 3].isFree() == false) {
					
					//MAKE SURE GRAND EXCHANGE IS OPEN (must be open before we cancel a sell offer)
					check_error();
					
					int num_items_just_purchased = GeInteract.count_num_items(the_items[i].item_id);
					//log("num_items_just_purchased:" + num_items_just_purchased);
					int num_items_offered_to_sell = the_buttons[i+3].num_items;
					//log("num_items_offered_to_sell:" + num_items_offered_to_sell);
					GeInteract.view_sell_offer(i + 3);
					GeInteract.abort_sell_offer();														
					int num_items_actually_sold = (num_items_just_purchased + num_items_offered_to_sell) - GeInteract.count_num_items(the_items[i].item_id);
					//log("num_items_actually_sold" + num_items_actually_sold);
					the_buttons[i+3].setEmpty();
					// adjust selling prices
					if (timer != 7200000) {
						adjust_sell_price(num_items_offered_to_sell, num_items_actually_sold, i);
					}
				}
				
				//make sure that buying price is always lower than selling price
				the_items[i].swap_prices();
				
				//make sure our margin isn't too narrow
				the_items[i].widen_margin();
				
				// if we have enough cash, put up a buy offer
				if (GeInteract.get_inv_cash() > the_items[i].buy_price) {
					
					//MAKE SURE GRAND EXCHANGE IS OPEN (we must make sure the grand exchange is open before making a buy offer
					check_error();
					
					String item_name = the_items[i].name_item;
					int quantity = (GeInteract.get_inv_cash()/ the_items[i].buy_price);
					if (quantity >= the_items[i].limit) {
						quantity = the_items[i].limit;
					}
					
					int buy_price = the_items[i].buy_price;
					int button = i;

					if (quantity > 0) {
						//if we are buying 10, then the sell offer should be empty
						if((quantity + GeInteract.count_num_items(the_items[i].item_id)) >= the_items[i].limit){
							quantity = the_items[i].limit - GeInteract.count_num_items(the_items[i].item_id);
						}
						
						if(quantity >= the_items[i].limit){
							the_buttons[i+3].setEmpty();
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

						//if we are selling up to the limit, the buy offer should be empty.
						if(quantity >= the_items[i].limit){
							the_buttons[i].setEmpty();
						}
					}
				}
				
				//update the total cash, used for paint
				calculate_total_cash();
				
				log(Color.cyan, the_items[i].toString());
				
				//add additional pause after we put up first buy offers in the top row of the GE
				if(timer != 7200000){
					pause(25000);
				}
				
				
				if(check_error() == true){
					break;
				}
			}
			timer -= 1000;
			if(check_error() == true){
				break;
		}
		
		}
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
			pct_adjust *= 1.5;
		}else{
			pct_adjust *= 0.5;
		}
		the_items[the_index].sell_price *= (1 + pct_adjust);
		
	}
	
	public void adjust_buy_price(int offered_to_buy, int actually_bought, int the_index){
		double materiality = the_items[the_index].determine_materiality();
		double pct_adjust = -1 * ((4/offered_to_buy)*actually_bought - 2) * materiality;
		if(offered_to_buy >= the_items[the_index].limit && actually_bought == 0){
			pct_adjust *= 1.5;
		}else{
			pct_adjust *= 0.5;
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
}// end brackets

