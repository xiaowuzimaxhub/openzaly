/** 
 * Copyright 2018-2028 Akaxin Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.akaxin.site.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akaxin.common.command.Command;
import com.akaxin.common.command.CommandResponse;
import com.akaxin.common.constant.CommandConst;
import com.akaxin.common.constant.HttpUriAction;
import com.akaxin.site.business.api.IRequest;
import com.akaxin.site.business.impl.hai.HttpFriendService;
import com.akaxin.site.business.impl.hai.HttpGroupService;
import com.akaxin.site.business.impl.hai.HttpMessageService;
import com.akaxin.site.business.impl.hai.HttpPluginService;
import com.akaxin.site.business.impl.hai.HttpPushService;
import com.akaxin.site.business.impl.hai.HttpSiteConfigService;
import com.akaxin.site.business.impl.hai.HttpUICService;
import com.akaxin.site.business.impl.hai.HttpUserService;
import com.akaxin.site.business.impl.site.SiteConfig;

/**
 * Http处理业务逻辑,部分功能需要先验证管理员权限
 * 
 * @author Sam{@link an.guoyue254@gmail.com}
 * @since 2017.11.28
 *
 */
public class HttpRequestService implements IRequest {
	private static final Logger logger = LoggerFactory.getLogger(HttpRequestService.class);

	public CommandResponse process(Command command) {
		HttpUriAction huaEnum = HttpUriAction.getUriActionEnum(command.getRety(), command.getService());
		CommandResponse response = null;
		try {
			switch (huaEnum) {
			case HAI_SITE_SERVICE:
				response = new HttpSiteConfigService().execute(command);
				break;
			case HAI_USER_SERVICE:
				response = new HttpUserService().execute(command);
				break;
			case HAI_GROUP_SERVICE:
				response = new HttpGroupService().execute(command);
				break;
			case HAI_FRIEND_SERVICE:
				response = new HttpFriendService().execute(command);
				break;
			case HAI_MESSAGE_SERVICE:
				response = new HttpMessageService().execute(command);
				break;
			case HAI_PUSH_SERVICE:
				response = new HttpPushService().execute(command);
				break;
			case HAI_PLUGIN_SERVICE:
				response = new HttpPluginService().execute(command);
				break;
			case HAI_UIC_SERVICE:
				response = new HttpUICService().execute(command);
				break;
			default:
				logger.error("error http request command={}", command.toString());
				break;
			}
		} catch (Exception e) {
			logger.error("http request service error.", e);
		}
		if (response == null) {
			response = new CommandResponse();
		}
		return response.setVersion(CommandConst.PROTOCOL_VERSION).setAction(CommandConst.ACTION_RES);
	}

	private boolean checkPermissions(String siteUserId) {
		boolean result = SiteConfig.isSiteManager(siteUserId);
		logger.debug("check plugin permission result={}", result);
		return result;
	}
}
