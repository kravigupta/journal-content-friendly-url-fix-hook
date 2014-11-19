package me.rkg.plugins;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.journal.NoSuchArticleException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceWrapper;
import com.liferay.portlet.journal.service.JournalArticleLocalService;

/**
 * @author Ravi Kumar Gupta
 * 
 * Purpose of this hook is to remove all special characters from urlTitle of journal article and keep only alphanumeric string with (-)DASHes.
 * 
 */
public class MyJournalArticleLocalServiceImpl extends JournalArticleLocalServiceWrapper {

	public MyJournalArticleLocalServiceImpl(JournalArticleLocalService journalArticleLocalService) {
		// TODO Auto-generated constructor stub
		super(journalArticleLocalService);
	}

	@Override
	public JournalArticle addArticle(long userId, long groupId, long classNameId, long classPK, String articleId,
			boolean autoArticleId, double version, Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			String content, String type, String structureId, String templateId, String layoutUuid,
			int displayDateMonth, int displayDateDay, int displayDateYear, int displayDateHour, int displayDateMinute,
			int expirationDateMonth, int expirationDateDay, int expirationDateYear, int expirationDateHour,
			int expirationDateMinute, boolean neverExpire, int reviewDateMonth, int reviewDateDay, int reviewDateYear,
			int reviewDateHour, int reviewDateMinute, boolean neverReview, boolean indexable, boolean smallImage,
			String smallImageURL, File smallImageFile, Map<String, byte[]> images, String articleURL,
			ServiceContext serviceContext) throws PortalException, SystemException {
		JournalArticle article = super.addArticle(userId, groupId, classNameId, classPK, articleId, autoArticleId,
				version, titleMap, descriptionMap, content, type, structureId, templateId, layoutUuid,
				displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute,
				expirationDateMonth, expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute,
				neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute,
				neverReview, indexable, smallImage, smallImageURL, smallImageFile, images, articleURL, serviceContext);

		String urlTitle = article.getUrlTitle();
		if (_log.isDebugEnabled()) {
			_log.debug("urlTitle before change: " + urlTitle);
		}

		urlTitle = urlTitle.replaceAll("[^a-zA-Z0-9_-]+", "");

		if (_log.isDebugEnabled()) {
			_log.debug("urlTitle after replacement: " + urlTitle);
		}
		
		// Running the loop in case the urlTitle after replacement already exists for some other article.
		for (int i = 1;; i++) {
			JournalArticle article1 = null;

			try {
				article1 = getArticleByUrlTitle(groupId, urlTitle);
			} catch (NoSuchArticleException nsae) {
			}

			if ((article1 == null) || articleId.equals(article1.getArticleId())) {
				break;
			} else {
				String suffix = StringPool.DASH + i;

				String prefix = urlTitle;

				if (urlTitle.length() > suffix.length()) {
					prefix = urlTitle.substring(0, urlTitle.length() - suffix.length());
				}

				urlTitle = prefix + suffix;
			}
		}
		// updating existing article with new urlTitle
		article.setUrlTitle(urlTitle);
		super.updateJournalArticle(article);

		if (_log.isDebugEnabled()) {
			_log.debug("Updated urlTitle via hook : " + article.getUrlTitle());
		}
		return article;
	}

	private static Log _log = LogFactoryUtil.getLog(MyJournalArticleLocalServiceImpl.class);
}
