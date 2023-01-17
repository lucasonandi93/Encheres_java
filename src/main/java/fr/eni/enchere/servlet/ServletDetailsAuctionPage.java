package fr.eni.enchere.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import fr.eni.enchere.bll.ArticleManager;
import fr.eni.enchere.bll.AuctionManager;
import fr.eni.enchere.bo.Article;
import fr.eni.enchere.bo.Auction;
import fr.eni.enchere.exceptions.BusinessException;

/**
 * Servlet implementation class ServletConnexionPage
 */
@WebServlet("/ServletDetailsAuctionPage")
public class ServletDetailsAuctionPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public ServletDetailsAuctionPage() throws ServletException {
        super();
    } 

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ArticleManager articleManager = new ArticleManager();
		AuctionManager auctionManager = new AuctionManager();
		
		try {
			Article articleOngoing = articleManager.selectById(Integer.parseInt(request.getParameter("articleID")));
			request.setAttribute("articleOngoing", articleOngoing);
			
			List<Auction> auctions = auctionManager.selectByNoArticle(Integer.parseInt(request.getParameter("articleID")));
		if (!auctions.isEmpty()) {
			System.out.println("A");
			Auction auctionOngoing = new Auction();
			for (Auction auction : auctions) {
				if(auction.getAuctionAmount()>auctionOngoing.getAuctionAmount()) {
					auctionOngoing = auction;
				}
			}
			request.setAttribute("pseudoBestAuction", auctionOngoing.getUser().getPseudo());
		}else {
			System.out.println("B");
			request.setAttribute("pseudoBestAuction", articleOngoing.getUser().getPseudo());
		}
		
		} catch (NumberFormatException | BusinessException e) {
			e.printStackTrace();
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/detailsAuctionPage.jsp");
		rd.forward(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
