package supermarket.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.swing.JOptionPane;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.wb.swt.ResourceManager;

import supermarket.dialog.VipDelete;
import supermarket.dialog.VipPayMoney;
import supermarket.dialog.VipRePassword;
import supermarket_object.Product;
import supermarket_object.ProductInBag;
import supermarket_object.ProductManager;
import supermarket_object.Type;
import supermarket_object.TypeManager_db;
import supermarket_object.Vip;
import supermarket_object.VipManager;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class VipView extends ViewPart {

	public static final String ID = "supermarket.views.VipView"; //$NON-NLS-1$
	public static Vip vip = null;
	private Table table;
	private Table table_1;
	private Label lblVip_1;
	private Label label_4;
	private Label lblDate;
	private Label lblTime;
	private Product p;
	private float money = 0.0f;
	private List<Product> productsOnShalves = new ArrayList<Product>();
	private Set<Product> products = new HashSet<Product>();
	private Stack<Product> Bag = new Stack<Product>();
	private List<ProductInBag> productsInBag = new ArrayList<ProductInBag>();

	public VipView() {
		Viptime();
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		final Combo combo = new Combo(container, SWT.NONE);
		combo.setBounds(64, 15, 88, 25);
		TypeManager_db TManager = new TypeManager_db();
		for (Type student : TManager.queryAll()) {
			combo.add(student.getName());
		}

		Label label = new Label(container, SWT.NONE);
		label.setBounds(10, 18, 61, 17);
		label.setText("\u79CD\u7C7B\u5217\u8868");

		final TableViewer tableViewer = new TableViewer(container, SWT.BORDER
				| SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setBounds(10, 73, 314, 165);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setWidth(40);
		tableColumn.setText("\u7F16\u53F7");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_1 = tableViewerColumn_1.getColumn();
		tableColumn_1.setWidth(60);
		tableColumn_1.setText("\u79CD\u7C7B");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_2 = tableViewerColumn_2.getColumn();
		tableColumn_2.setWidth(90);
		tableColumn_2.setText("\u5546\u54C1\u540D\u79F0");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_3 = tableViewerColumn_3.getColumn();
		tableColumn_3.setWidth(46);
		tableColumn_3.setText("\u4F59\u91CF");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_4 = tableViewerColumn_4.getColumn();
		tableColumn_4.setWidth(72);
		tableColumn_4.setText("\u5355\u4EF7");
		// 注册内容提供器
		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@SuppressWarnings("rawtypes")
			public Object[] getElements(Object inputElement) {
				return ((List) inputElement).toArray();// 这里传入的数据是一张链表
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
		});
		// 注册内容显示器
		tableViewer.setLabelProvider(new ITableLabelProvider() {
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				Product person = (Product) element;
				switch (columnIndex) {
				case 0:
					return String.valueOf(person.getPid()); // 第一列显示Pid
				case 1:
					return person.getType().getName(); // 第二列显示种类
				case 2:
					return person.getName(); // 第二列显示商品名称
				case 3:
					return String.valueOf(person.getMargin()); // 第三列显示余量
				case 4:
					return String.valueOf(person.getPrice()); // 第三列显示单价
				}
				return null;
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void addListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub

			}
		});

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setBounds(10, 50, 61, 17);
		label_1.setText("\u5546\u54C1\u5217\u8868");

		Label label_2 = new Label(container, SWT.NONE);
		label_2.setBounds(10, 244, 61, 17);
		label_2.setText("\u8D2D\u7269\u8F66");

		final TableViewer tableViewer_1 = new TableViewer(container, SWT.BORDER
				| SWT.FULL_SELECTION);
		table_1 = tableViewer_1.getTable();
		table_1.setHeaderVisible(true);
		table_1.setBounds(10, 267, 314, 137);

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(
				tableViewer_1, SWT.NONE);
		TableColumn tableColumn_5 = tableViewerColumn_5.getColumn();
		tableColumn_5.setWidth(40);
		tableColumn_5.setText("\u7F16\u53F7");

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(
				tableViewer_1, SWT.NONE);
		TableColumn tableColumn_6 = tableViewerColumn_6.getColumn();
		tableColumn_6.setWidth(90);
		tableColumn_6.setText("\u5546\u54C1\u540D\u79F0");

		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(
				tableViewer_1, SWT.NONE);
		TableColumn tableColumn_7 = tableViewerColumn_7.getColumn();
		tableColumn_7.setWidth(50);
		tableColumn_7.setText("\u6570\u91CF");

		TableViewerColumn tableViewerColumn_8 = new TableViewerColumn(
				tableViewer_1, SWT.NONE);
		TableColumn tableColumn_8 = tableViewerColumn_8.getColumn();
		tableColumn_8.setWidth(50);
		tableColumn_8.setText("\u5355\u4EF7");

		TableViewerColumn tableViewerColumn_9 = new TableViewerColumn(
				tableViewer_1, SWT.NONE);
		TableColumn tableColumn_9 = tableViewerColumn_9.getColumn();
		tableColumn_9.setWidth(78);
		tableColumn_9.setText("\u91D1\u989D");

		// 注册内容提供器
		tableViewer_1.setContentProvider(new IStructuredContentProvider() {
			@SuppressWarnings("rawtypes")
			public Object[] getElements(Object inputElement) {
				return ((List) inputElement).toArray();// 这里传入的数据是一张链表
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
		});
		// 注册内容显示器
		tableViewer_1.setLabelProvider(new ITableLabelProvider() {
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				ProductInBag person = (ProductInBag) element;
				switch (columnIndex) {
				case 0:
					return String.valueOf(person.getId()); // 第一列显示Pid
				case 1:
					return person.getName(); // 第二列显示种类
				case 2:
					return String.valueOf(person.getCount()); // 第二列显示数量
				case 3:
					return String.valueOf(person.getPrice()); // 第三列显示单价
				case 4:
					return String.valueOf(person.getCount() * person.getPrice()); // 第三列显示金额
				}
				return null;
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void addListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub

			}
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent arg0) {
				// TODO Auto-generated method stub
				IStructuredSelection iss = (IStructuredSelection) arg0
						.getSelection();
				// 得到记录的实体对象（要类型转换）
				p = (Product) iss.getFirstElement();
				if (p.getMargin() != 0) {
					for (int i = 0; i < productsOnShalves.size(); i++) {
						if (productsOnShalves.get(i).equals(p)) {
							productsOnShalves.get(i).setMargin(
									productsOnShalves.get(i).getMargin() - 1);
							break;
						}
					}
					ProductManager PManager = new ProductManager();
					PManager.updata(p.getPid(), p);
					Bag.push(p);
					ProductInBag PIBag = new ProductInBag();
					PIBag.setId(p.getPid());
					PIBag.setName(p.getName());
					PIBag.setCount(0);
					PIBag.setPrice(p.getPrice());
					boolean isExsit = false;
					if (productsInBag.size() == 0) {
						productsInBag.add(PIBag);
					}
					for (int i = 0; i < productsInBag.size(); i++) {
						ProductInBag SRP3 = productsInBag.get(i);
						if (SRP3.getName().equals(PIBag.getName())) {
							productsInBag.get(i).setCount(
									productsInBag.get(i).getCount() + 1);
							isExsit = true;
							break;
						}
					}
					if (!isExsit) {
						PIBag.setCount(1);
						productsInBag.add(PIBag);
					}
					tableViewer_1.setInput(productsInBag);
					tableViewer.refresh();
					products.add(p);
					money = money + p.getPrice();
				} else {
					JOptionPane.showMessageDialog(null, "该商品已售完！");
				}
			}
		});

		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProductManager PManager = new ProductManager();
				productsOnShalves = PManager.queryAll();
				tableViewer.setInput(productsOnShalves);
			}
		});
		btnNewButton.setBounds(244, 13, 80, 27);
		btnNewButton.setText("\u67E5\u770B\u5168\u90E8");

		Button btnNewButton_1 = new Button(container, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!combo.getText().equals("")) {
					ProductManager PManager = new ProductManager();
					List<Product> List = PManager.query3(combo.getText());
					productsOnShalves = List;
					tableViewer.setInput(productsOnShalves);
				} else {
					JOptionPane.showMessageDialog(null, "请选择种类！", "错误",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton_1.setBounds(158, 13, 80, 27);
		btnNewButton_1.setText("\u786E\u5B9A");
		final Shell shl = this.getViewSite().getShell();
		Button button = new Button(container, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				VipPayMoney dd = new VipPayMoney(shl, SWT.MAX | SWT.MIN);
				VipManager VManager = new VipManager();
				VipView.this.getViewSite().getShell().setEnabled(false);
				dd.open(money, products, productsInBag, vip);
				vip = VManager.query(vip.getVid());
				lblVip_1.setText("\u7B49\u7EA7\uFF1A  " + vip.getLevel()
						+ "  leve");
				label_4.setText("\u8DDD\u79BB\u5347\u7EA7\uFF1A"
						+ String.valueOf((100 + vip.getLevel() * 100)
								* (vip.getLevel()) / 2 - vip.getExperience())
						+ "\u5143");
				Bag.clear();
				money = 0.0f;
				products.clear();
				productsInBag.clear();
				tableViewer_1.refresh();
				VipView.this.getViewSite().getShell().setEnabled(true);
			}
		});
		button.setBounds(10, 417, 80, 27);
		button.setText("\u7ED3\u7B97");

		Button button_1 = new Button(container, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!productsInBag.isEmpty()) {
					ProductManager PManager = new ProductManager();
					List<Product> ALL = PManager.queryAll();
					for (ProductInBag PIB : productsInBag) {
						if (PIB.getName().equals(Bag.peek().getName())) {
							if (PIB.getCount() == 1) {
								productsInBag.remove(PIB);
								products.remove(Bag.peek());
								if (money > 0) {
									money = money - Bag.peek().getPrice();
								}
								break;
							} else {
								PIB.setCount(PIB.getCount() - 1);
								money = money - Bag.peek().getPrice();
							}
						}
					}
					for (int i = 0; i < ALL.size(); i++) {
						if (ALL.get(i).getName().equals(Bag.peek().getName())) {
							ALL.get(i).setMargin(ALL.get(i).getMargin() + 1);
							PManager.updata(Bag.peek().getPid(), ALL.get(i));
							break;
						}
					}
					for (int i = 0; i < productsOnShalves.size(); i++) {
						if (productsOnShalves.get(i).getName()
								.equals(Bag.peek().getName())) {
							productsOnShalves.get(i).setMargin(
									productsOnShalves.get(i).getMargin() + 1);
						}
					}
					Bag.pop();
					tableViewer_1.refresh();
					tableViewer.refresh();
				} else {
					JOptionPane.showMessageDialog(null, "购物车内无物品！", "错误",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button_1.setBounds(128, 417, 80, 27);
		button_1.setText("\u64A4\u9500");

		Button button_2 = new Button(container, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProductManager PManager = new ProductManager();
				for (int i = 0; i < productsInBag.size(); i++) {
					Product pro = PManager.query(productsInBag.get(i).getId());
					pro.setMargin(pro.getMargin()
							+ productsInBag.get(i).getCount());
					PManager.updata(pro.getPid(), pro);
					for (int j = 0; j < productsOnShalves.size(); j++) {
						if (productsOnShalves.get(j).getPid() == pro.getPid()) {
							productsOnShalves.get(j).setMargin(pro.getMargin());
						}
					}
				}
				productsInBag.clear();
				Bag.clear();
				tableViewer_1.refresh();
				tableViewer.refresh();
				products.clear();
				money = 0f;
			}
		});
		button_2.setBounds(244, 417, 80, 27);
		button_2.setText("\u6E05\u7A7A");

		Button button_3 = new Button(container, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (productsInBag.isEmpty()) {
					IWorkbenchPage page = VipView.this.getViewSite().getPage();
					interfaceView.Timer = true;
					page.hideView(page.findView(ID));
					vip = null;
					try {
						page.showView(mianView.ID);
						page.showView(interfaceView.ID);
					} catch (PartInitException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "请先结算或清空购物车物品!", "提示",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button_3.setBounds(369, 417, 80, 27);
		button_3.setText("\u9000\u51FA");

		Button button_4 = new Button(container, SWT.NONE);
		button_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				VipRePassword dd = new VipRePassword(shl, SWT.MAX | SWT.MIN);
				VipView.this.getViewSite().getShell().setEnabled(false);
				dd.open(vip);
				VipView.this.getViewSite().getShell().setEnabled(true);
			}
		});
		button_4.setBounds(369, 364, 80, 27);
		button_4.setText("\u4FEE\u6539\u5BC6\u7801");

		Button button_5 = new Button(container, SWT.NONE);
		button_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (productsInBag.isEmpty()) {
					VipDelete dd = new VipDelete(shl, SWT.MAX | SWT.MIN);
					VipView.this.getViewSite().getShell().setEnabled(false);
					boolean state = dd.open(vip);
					VipView.this.getViewSite().getShell().setEnabled(true);
					if (state) {
						IWorkbenchPage page = VipView.this.getViewSite()
								.getPage();
						interfaceView.Timer = true;
						page.hideView(page.findView(ID));
						vip = null;
						try {
							page.showView(mianView.ID);
							page.showView(interfaceView.ID);
						} catch (PartInitException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "请先结算或清空购物车物品!", "提示",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button_5.setBounds(369, 313, 80, 27);
		button_5.setText("\u6CE8\u9500\u8D26\u53F7");

		Label lblVip = new Label(container, SWT.NONE);
		lblVip.setBounds(369, 127, 97, 17);
		lblVip.setText("\u7528\u6237\u540D\uFF1A  " + vip.getName());

		lblVip_1 = new Label(container, SWT.NONE);
		lblVip_1.setBounds(369, 150, 115, 17);
		lblVip_1.setText("\u7B49\u7EA7\uFF1A  " + vip.getLevel() + "  leve");

		Label lblVip_2 = new Label(container, SWT.NONE);
		lblVip_2.setBounds(369, 196, 104, 17);
		lblVip_2.setText("\u751F\u65E5\uFF1A" + vip.getBirthday());

		Label label_3 = new Label(container, SWT.NONE);
		label_3.setBounds(369, 233, 61, 17);
		label_3.setText("\u5F53\u524D\u65F6\u95F4\uFF1A");

		Label lblLogo = new Label(container, SWT.NONE);
		lblLogo.setImage(ResourceManager.getPluginImage("SuperMarket_Rcp", "icons/vipLogo.jpg"));
		lblLogo.setBounds(319, 0, 175, 121);

		label_4 = new Label(container, SWT.NONE);
		label_4.setBounds(369, 173, 115, 17);
		label_4.setText("\u8DDD\u79BB\u5347\u7EA7\uFF1A"
				+ String.valueOf((100 + vip.getLevel() * 100)
						* (vip.getLevel()) / 2 - vip.getExperience())
				+ "\u5143");

		lblTime = new Label(container, SWT.NONE);
		lblTime.setBounds(383, 274, 80, 17);
		lblTime.setText("time");

		lblDate = new Label(container, SWT.NONE);
		lblDate.setBounds(362, 255, 122, 17);
		lblDate.setText("date");
		
		ProductManager PManager = new ProductManager();
		productsOnShalves = PManager.queryAll();
		tableViewer.setInput(productsOnShalves);

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	public void Viptime() {
		class VipThread extends Thread {
			public void run() {
				while (!interfaceView.Timer) {

					// 异步执行一段代码
					PlatformUI.getWorkbench().getDisplay()
							.asyncExec(new Runnable() {
								public void run() {
									Calendar now = new GregorianCalendar();
									int hour = now.get(Calendar.HOUR_OF_DAY); // 得到小时数
									int minute = now.get(Calendar.MINUTE); // 得到分数
									int second = now.get(Calendar.SECOND); // 得到秒数
									int year = now.get(Calendar.YEAR);
									int month = now.get(Calendar.MONTH) + 1;
									int day = now.get(Calendar.DAY_OF_MONTH);
									int day2 = now.get(Calendar.WEDNESDAY);
									String weekend = "";
									switch (day2) {
									case 0: {
										weekend = "日";
									}
									case 1: {
										weekend = "一";
									}
									case 2: {
										weekend = "二";
									}
									case 3: {
										weekend = "三";
									}
									case 4: {
										weekend = "四";
									}
									case 5: {
										weekend = "五";
									}
									case 6: {
										weekend = "六";
									}
									}
									String date = year + "-" + month + "-"
											+ day + "  星期：" + weekend;
									String timeInfo = "";
									if (hour <= 9)
										timeInfo += "0" + hour + ":"; // 格式化输出
									else
										timeInfo += hour + ":";
									if (minute <= 9)
										timeInfo += "0" + minute + ":";
									else
										timeInfo += minute + ":";
									if (second <= 9)
										timeInfo += "0" + second;
									else
										timeInfo += second;
									lblTime.setText(timeInfo);
									lblDate.setText(date);
								}
							});

					try {
						Thread.sleep(1000);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
		VipThread mt = new VipThread();
		mt.start();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		@SuppressWarnings("unused")
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		@SuppressWarnings("unused")
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
