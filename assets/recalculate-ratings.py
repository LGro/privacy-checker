#!/usr/bin/python

import numpy, sqlite3

# pass -1 for non existing expert/novice ratings
def calculateTotalPrivacyRating(auto,expert,novices):
	totalRating = 0

	if expert > -1 and novices > -1:
		# expert and non expert ratings exist
		totalRating = auto * 0.3 + expert * 0.5 + novices * 0.2;
	elif expert > -1:
		# only expert ratings exist
		totalRating = auto * 0.375 + expert * 0.625;
	elif novices > -1:
		# only non expert ratings exist
		totalRating = auto * 0.6 + novices * 0.4;
	else:
		# neither expert nor non expert ratings exist
		totalRating = auto;

	return totalRating;


# algorithm taken from android project
def calculateAutoRating(permissions):
	
	automaticPrivacyRating = 0.0
	numberOfNonCriticalPermissions = 0
	numberOfCriticalPermissions = 0
	
	pCount = 0
	
	for p in permissions:
		pCount += 1
		if p[0] >= 50:
			numberOfNonCriticalPermissions += 1
		else:
			numberOfCriticalPermissions += 1
			automaticPrivacyRating += p[0]
	
	if pCount == 0:
		return 5
	
	if numberOfCriticalPermissions == 0:
		if (numberOfNonCriticalPermissions == 0):
			return 5
		else:
			return 5 - (1 / (numpy.exp(0.15 * numberOfNonCriticalPermissions)))
	
	if numberOfCriticalPermissions != 0:
		automaticPrivacyRating /= numberOfCriticalPermissions
		automaticPrivacyRating /= 50
		automaticPrivacyRating *= 4
	else:
		automaticPrivacyRating = 4
	
	return automaticPrivacyRating + (1 / (numpy.exp(0.15 * numberOfNonCriticalPermissions)))


# algorithm taken from android project
def calculateWeightedAutoRating(categoryMean, autoRating):
	difference = abs(categoryMean - autoRating)
	weightedAutoRating = 0
	
	if autoRating > categoryMean:
		weightedAutoRating = (autoRating + difference * 0.4)
		if weightedAutoRating > 5:
			weightedAutoRating = 5
	elif autoRating < categoryMean:
		weightedAutoRating = (autoRating - difference * 0.8)
		if weightedAutoRating < 0:
			weightedAutoRating = 0
	
	return weightedAutoRating


conn = sqlite3.connect('pca.db')
print "Opened database successfully"
print "recalculating..."

# calculate automatic rating for each app
allAppsCursor = conn.execute("SELECT app_id FROM tbl_app ORDER BY app_id")
for app in allAppsCursor:
	
	permissionsCursor = conn.execute("SELECT criticality FROM tbl_permission JOIN tbl_app_permission ON tbl_permission.permission_id = tbl_app_permission.permission_id WHERE app_id =?", [app[0]])
	
	autoRating = calculateAutoRating(permissionsCursor)
   
	conn.execute("UPDATE tbl_app SET automatic_rating = ? WHERE app_id = ?", (autoRating, app[0]))
	conn.commit()


# calculate mean criticality for each category
allCategoriesCursor = conn.execute("SELECT category_id FROM tbl_category ORDER BY category_id ASC");
for category in allCategoriesCursor:
	
	#print "update category: " + `category[0]`
	
	# get auto rating mean for current category
	appsCursor = conn.execute("SELECT AVG(automatic_rating) FROM tbl_app WHERE category_id = " + str(category[0]))
	apps = appsCursor.fetchone()
	meanAutoRating = apps[0]
	
	# TODO: doing this commit here results in categories being recalculated multiple times
	conn.execute("UPDATE tbl_category SET average_auto_rating = ? WHERE category_id = ?", (meanAutoRating,category[0]))
	conn.commit()
	
	# calculate weighted auto rating and total privacy rating for each app
	allAppsCursor = conn.execute("SELECT app_id, automatic_rating FROM tbl_app WHERE category_id = " + str(category[0]) + " ORDER BY app_id")
	for app in allAppsCursor:
		
		# initialize default ratings
		expertRating = -1
		noviceRating = -1
		
		# get average and count of expert/novice ratings
		# TODO: remove '' from user_type value and maybe change 0 to 00 when database has changed user_type from TEXT to INTEGER
		expertRatingCursor = conn.execute("SELECT AVG(value), COUNT(rating_app_id) FROM tbl_rating_app WHERE user_type = '1' AND app_id = " + str(app[0]))
		expertRatings = expertRatingCursor.fetchone()
		noviceRatingCursor = conn.execute("SELECT AVG(value), COUNT(rating_app_id) FROM tbl_rating_app WHERE user_type = '0' AND app_id = " + str(app[0]))
		noviceRatings = noviceRatingCursor.fetchone()
		
		# if there are any ratings, update dafault ratings
		if expertRatings[1] > 0:
			expertRating = expertRatings[0]
		if noviceRatings[1] > 0:
			noviceRating = noviceRatings[1]
		
		# get weighted auto rating
		weightedAutoRating = calculateWeightedAutoRating(meanAutoRating, app[1])
		
		# get total privacy rating
		totalPrivacyRating = calculateTotalPrivacyRating(weightedAutoRating, expertRating, noviceRating)
		
		# update weighted auto rating and total privacy rating for app
		conn.execute("UPDATE tbl_app SET category_weighted_automatic_rating = ?, privacy_rating = ? WHERE app_id = ?", (weightedAutoRating, totalPrivacyRating, app[0]))
		conn.commit()


print "Operation done successfully"

conn.close()
