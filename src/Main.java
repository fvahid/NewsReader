
import org.eclipse.swt.widgets.Display;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.awt.TextArea;
import java.io.IOException;
import java.net.InetAddress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;


public class Main {

	protected Shell shlNewsApplication;
	private Text titileresult;
	private Text sitename;
	private Text text_1;
	private Button buttonclear;
	private Button buttonfilter;
	private InputDialog filterbox;
	private String filtertext;
	private Text text_filter;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Main window = new Main();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlNewsApplication.open();
		shlNewsApplication.layout();
		while (!shlNewsApplication.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width-1) + ".";
		else
			return s;
	}



	protected void createContents() {
		shlNewsApplication = new Shell();
		shlNewsApplication.setSize(907, 531);
		shlNewsApplication.setText("News Application");

		Button btnShow = new Button(shlNewsApplication, SWT.NONE);
		btnShow.setBounds(259, 40, 87, 73);
		btnShow.setText("Show ");

		titileresult = new Text(shlNewsApplication, SWT.BORDER);
		titileresult.setBounds(36, 94, 201, 19);

		sitename = new Text(shlNewsApplication, SWT.BORDER);
		sitename.setBounds(36, 42, 201, 19);
		sitename.setText("http://google.com");

		//		Label lblNewLabel = new Label(shell, SWT.NONE);
		//		lblNewLabel.setBounds(48, 125, 279, 113);
		//		lblNewLabel.setText("New Label");

		Text text = new Text(shlNewsApplication,  SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL );
		text.setFont(SWTResourceManager.getFont("Tahoma", 9, SWT.NORMAL));
		text.setEditable(false);
		text.setBounds(36, 119, 417, 328);

		text_1 = new Text(shlNewsApplication, SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.RIGHT);
		text_1.setFont(SWTResourceManager.getFont("Tahoma", 9, SWT.NORMAL));
		text_1.setBounds(458, 119, 417, 328);

		buttonclear = new Button(shlNewsApplication, SWT.NONE);
		buttonclear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				text_1.setText("");
				titileresult.setText("");
				text_filter.setText("");
			}
		});
		buttonclear.setText("Clear");
		buttonclear.setBounds(352, 40, 87, 73);

		buttonfilter = new Button(shlNewsApplication, SWT.NONE);
		buttonfilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				filterbox = new InputDialog(shlNewsApplication);
				filterbox.open();
				filtertext = filterbox.getInput();
				text_filter.setText(filtertext);
			}
		});
		buttonfilter.setText("filter");
		buttonfilter.setBounds(445, 40, 87, 73);

		text_filter = new Text(shlNewsApplication, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text_filter.setBounds(549, 55, 76, 26);


		btnShow.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				text.setText("");
				text_1.setText("");
				titileresult.setText("");
				Document doc;
				try {
					String Sitename = "http://google.com";
					if (sitename.getText().length() > 0)
						Sitename = sitename.getText();
					//doc = Jsoup.connect("http://mehrabad.airport.ir/schedule").get();
					doc = Jsoup.connect(Sitename).timeout(50000).get();
					Elements links = doc.select("a[href]");
					Elements media = doc.select("[src]");
					Elements imports = doc.select("link[href]");
					String title = doc.title();
					titileresult.setText(title);

					//Document doc = Jsoup.parse(html);
					String whtml = doc.toString();
					//System.out.print(whtml);
					Document doc1 = Jsoup.parse(whtml);
					Element body = doc.body();

					print("\nMedia: (%d)", media.size());
					for (Element src : media) {
						if (src.tagName().equals("img"))
							//							print(" * %s: <%s> %sx%s (%s)",
							//									src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
							//									trim(src.attr("alt"), 20));
							;
						else
							//print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
							;
					}

					print("\nImports: (%d)", imports.size());
					for (Element link : imports) {
						//print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
						;
					}

					print("\nLinks: (%d)", links.size());
					for (Element link : links) {
						//print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
						if(link.attr("abs:href").contains(filtertext)) 
						{
							text.append(link.attr("abs:href")+"\n");
							text_1.append(link.text()+"\n");
						}

					}
				}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

	}
}
