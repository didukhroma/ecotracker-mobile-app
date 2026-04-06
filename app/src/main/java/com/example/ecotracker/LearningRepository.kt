package com.example.ecotracker

import android.content.Context

data class LearningCategory(
    val id: String,
    val title: String,
    val lessons: List<LearningLesson>
)

data class LearningLesson(
    val id: String,
    val categoryId: String,
    val title: String,
    val shortDescription: String,
    val imageAsset: String,
    val introText: String,
    val sectionOneTitle: String,
    val sectionOneBody: String,
    val sectionTwoTitle: String,
    val sectionTwoBody: String,
    val bottomImageAsset: String? = null,
    val bottomCaption: String? = null
)

object LearningRepository {
    fun getCategories(): List<LearningCategory> {
        return listOf(
            LearningCategory(
                id = "transport",
                title = "Transport",
                lessons = listOf(
                    LearningLesson(
                        id = "transport_1",
                        categoryId = "transport",
                        title = "The Power of Public Transport",
                        shortDescription = "Public transport reduces emissions and improves urban mobility.",
                        imageAsset = "learning/transport-lesson-1.jpg",
                        introText = "Public transport is one of the most effective tools for building sustainable cities.",
                        sectionOneTitle = "Why Public Transport Matters",
                        sectionOneBody = "Public transport reduces the number of private cars on the road. This leads to lower greenhouse gas emissions and cleaner air. Buses and trains carry many people at once, making them energy efficient. Reduced traffic congestion improves travel times for everyone. Cities become quieter and safer with fewer vehicles. Public transport is also more affordable for many people. It increases access to jobs and education. Efficient systems support economic growth. Urban planning becomes easier with strong transport networks. Choosing public transport benefits both individuals and society.",
                        sectionTwoTitle = "How to Start Using It",
                        sectionTwoBody = "Begin by identifying your most frequent routes. Check local transport schedules and apps. Try replacing one car trip per week. Gradually increase your usage. Plan ahead to reduce waiting times. Use travel cards to save money. Combine walking with public transport. Learn peak hours and avoid them when possible. Invite friends or coworkers to join. Consistency helps turn it into a habit.",
                        bottomImageAsset = "learning/transport-lesson-1.jpg",
                        bottomCaption = "Shared transport reduces emissions."
                    ),
                    LearningLesson(
                        id = "transport_2",
                        categoryId = "transport",
                        title = "Carpooling Basics",
                        shortDescription = "Sharing rides cuts emissions and saves money.",
                        imageAsset = "learning/transport-lesson-2.jpg",
                        introText = "Carpooling is a simple and effective sustainability habit.",
                        sectionOneTitle = "Environmental Impact",
                        sectionOneBody = "Carpooling reduces the number of cars on the road. This lowers emissions and fuel consumption. Traffic congestion decreases significantly. Fewer cars mean less air pollution. Shared rides improve efficiency of each trip. Infrastructure wear is also reduced. Parking demand becomes lower. Cities benefit from less crowded roads. Carpooling is easy to adopt. It delivers quick environmental benefits.",
                        sectionTwoTitle = "How to Organize It",
                        sectionTwoBody = "Start with coworkers or neighbors. Identify shared routes and schedules. Use apps or group chats to coordinate. Share costs fairly. Create a regular schedule. Be flexible when needed. Ensure comfort for all passengers. Rotate drivers if possible. Expand your group over time. Keep communication clear and consistent.",
                        bottomImageAsset = "learning/transport-lesson-2.jpg",
                        bottomCaption = "More people, fewer cars."
                    ),
                    LearningLesson(
                        id = "transport_3",
                        categoryId = "transport",
                        title = "Walking as Transport",
                        shortDescription = "Walking is the simplest zero-emission mobility option.",
                        imageAsset = "learning/transport-lesson-3.jpg",
                        introText = "Walking is often overlooked but highly effective.",
                        sectionOneTitle = "Benefits of Walking",
                        sectionOneBody = "Walking produces no emissions. It improves physical health. It reduces stress and improves mood. Cities become quieter with more pedestrians. Short trips are ideal for walking. It requires no special equipment. It is accessible to most people. Walking reduces dependency on vehicles. It helps reduce congestion. It supports sustainable lifestyles.",
                        sectionTwoTitle = "Making It a Habit",
                        sectionTwoBody = "Start with short distances. Walk to nearby shops or work. Choose safe routes. Wear comfortable shoes. Track steps for motivation. Combine walking with other transport. Walk with friends or family. Explore your neighborhood. Increase distance gradually. Make walking part of daily routine.",
                        bottomImageAsset = "learning/transport-lesson-3.jpg",
                        bottomCaption = "Every step matters."
                    ),
                    LearningLesson(
                        id = "transport_4",
                        categoryId = "transport",
                        title = "Cycling for Daily Commute",
                        shortDescription = "Cycling is fast, healthy, and eco-friendly.",
                        imageAsset = "learning/transport-lesson-4.jpg",
                        introText = "Cycling combines efficiency with sustainability.",
                        sectionOneTitle = "Why Choose Cycling",
                        sectionOneBody = "Cycling produces zero emissions. It is faster than walking for medium distances. It improves fitness and health. It reduces congestion in cities. Cycling infrastructure is expanding globally. It lowers transport costs. It requires less space than cars. It is ideal for urban commuting. It supports cleaner air. Cycling is a long-term sustainable habit.",
                        sectionTwoTitle = "Getting Started",
                        sectionTwoBody = "Choose a reliable bicycle. Use safety gear like helmets. Plan safe routes. Start with short trips. Learn local traffic rules. Maintain your bike regularly. Combine cycling with public transport. Track your rides. Stay visible at night. Practice consistency.",
                        bottomImageAsset = "learning/transport-lesson-4.jpg",
                        bottomCaption = "Ride toward sustainability."
                    ),
                    LearningLesson(
                        id = "transport_5",
                        categoryId = "transport",
                        title = "Electric Vehicles",
                        shortDescription = "EVs reduce pollution compared to traditional cars.",
                        imageAsset = "learning/transport-lesson-5.jpg",
                        introText = "Electric vehicles are transforming transport systems.",
                        sectionOneTitle = "Advantages of EVs",
                        sectionOneBody = "EVs produce no direct emissions. They improve air quality. They are more energy efficient. Maintenance is often lower. Renewable energy can power them. Noise pollution is reduced. Governments support EV adoption. Charging networks are expanding. They reduce dependence on fossil fuels. EVs are key for future mobility.",
                        sectionTwoTitle = "Things to Know",
                        sectionTwoBody = "Check charging infrastructure availability. Understand battery range. Compare total ownership cost. Plan long trips carefully. Explore incentives. Learn charging options. Consider home charging setup. Stay updated on technology. Evaluate your needs. Make informed decisions.",
                        bottomImageAsset = "learning/transport-lesson-5.jpg",
                        bottomCaption = "Driving cleaner."
                    ),
                    LearningLesson(
                        id = "transport_6",
                        categoryId = "transport",
                        title = "Reducing Air Travel Impact",
                        shortDescription = "Flights have high emissions, but choices matter.",
                        imageAsset = "learning/transport-lesson-6.jpg",
                        introText = "Air travel is one of the largest contributors to personal emissions.",
                        sectionOneTitle = "Why Flights Matter",
                        sectionOneBody = "Air travel produces significant CO2 emissions. Long flights have the biggest impact. Alternatives are often more sustainable. Aviation emissions are hard to reduce. Demand continues to grow globally. Awareness is increasing. Reducing flights lowers personal footprint. Choosing direct flights helps. Economy class is more efficient. Every decision matters.",
                        sectionTwoTitle = "Better Choices",
                        sectionTwoBody = "Fly less when possible. Choose trains for short distances. Combine trips to reduce frequency. Use carbon offset programs. Travel light to reduce fuel use. Choose airlines with better efficiency. Avoid unnecessary flights. Consider virtual meetings. Plan ahead. Travel consciously.",
                        bottomImageAsset = "learning/transport-lesson-6.jpg",
                        bottomCaption = "Fly smarter."
                    ),
                    LearningLesson(
                        id = "transport_7",
                        categoryId = "transport",
                        title = "Smart Route Planning",
                        shortDescription = "Efficient routes save time and energy.",
                        imageAsset = "learning/transport-lesson-7.jpg",
                        introText = "Planning routes can reduce environmental impact.",
                        sectionOneTitle = "Why Planning Matters",
                        sectionOneBody = "Efficient routes reduce fuel consumption. They save time and reduce stress. Avoiding traffic lowers emissions. Shorter trips mean less energy use. Planning helps combine errands. It improves productivity. Navigation apps provide real-time data. Better planning supports sustainability. It reduces unnecessary driving. It is easy to implement.",
                        sectionTwoTitle = "How to Plan",
                        sectionTwoBody = "Use navigation apps. Check traffic conditions. Combine multiple stops. Avoid peak hours. Choose shortest routes. Use public transport when possible. Plan weekly travel. Reduce unnecessary trips. Optimize daily routines. Stay flexible.",
                        bottomImageAsset = "learning/transport-lesson-7.jpg",
                        bottomCaption = "Plan smart, travel better."
                    ),
                    LearningLesson(
                        id = "transport_8",
                        categoryId = "transport",
                        title = "Shared Mobility Services",
                        shortDescription = "Bike and car sharing reduce ownership needs.",
                        imageAsset = "learning/transport-lesson-8.jpg",
                        introText = "Shared mobility is changing how we use transport.",
                        sectionOneTitle = "Benefits",
                        sectionOneBody = "Shared mobility reduces the need for private cars. It lowers emissions and costs. It improves urban space usage. Less parking is required. It increases flexibility. Services are widely available. It supports sustainable cities. Reduces resource consumption. Encourages efficient travel. It is growing globally.",
                        sectionTwoTitle = "How to Use",
                        sectionTwoBody = "Download sharing apps. Choose nearby vehicles. Pay per use. Follow usage rules. Return vehicles properly. Use when needed. Combine with other transport. Avoid unnecessary ownership. Explore different options. Build sustainable habits.",
                        bottomImageAsset = "learning/transport-lesson-8.jpg",
                        bottomCaption = "Access over ownership."
                    ),
                    LearningLesson(
                        id = "transport_9",
                        categoryId = "transport",
                        title = "Fuel Efficiency Driving",
                        shortDescription = "Driving smarter reduces fuel consumption.",
                        imageAsset = "learning/transport-lesson-9.jpg",
                        introText = "Driving habits have a big impact on emissions.",
                        sectionOneTitle = "Efficient Driving Techniques",
                        sectionOneBody = "Smooth acceleration saves fuel. Avoid sudden braking. Maintain steady speed. Use cruise control when possible. Keep tires properly inflated. Reduce unnecessary weight. Turn off engine when idle. Drive at optimal speeds. Regular maintenance improves efficiency. Smart driving reduces emissions.",
                        sectionTwoTitle = "Daily Improvements",
                        sectionTwoBody = "Plan trips in advance. Combine errands. Avoid traffic. Monitor fuel consumption. Adjust driving style gradually. Learn eco-driving techniques. Stay calm on the road. Use air conditioning wisely. Maintain vehicle regularly. Track progress.",
                        bottomImageAsset = "learning/transport-lesson-9.jpg",
                        bottomCaption = "Drive efficiently."
                    ),
                    LearningLesson(
                        id = "transport_10",
                        categoryId = "transport",
                        title = "Future of Sustainable Transport",
                        shortDescription = "New technologies will reshape mobility.",
                        imageAsset = "learning/transport-lesson-10.jpg",
                        introText = "Transport is evolving toward sustainability.",
                        sectionOneTitle = "Emerging Trends",
                        sectionOneBody = "Electric mobility is expanding rapidly. Autonomous vehicles are being developed. Smart cities integrate transport systems. Renewable energy powers mobility. High-speed rail is growing. Urban mobility is becoming shared. Technology improves efficiency. Digital tools optimize travel. Sustainability drives innovation. The future is cleaner and smarter.",
                        sectionTwoTitle = "Your Role",
                        sectionTwoBody = "Stay informed about new solutions. Adapt your habits gradually. Support sustainable policies. Choose eco-friendly options. Reduce unnecessary travel. Promote awareness. Encourage others. Use new technologies wisely. Be open to change. Every action contributes to the future.",
                        bottomImageAsset = "learning/transport-lesson-10.jpg",
                        bottomCaption = "The future is sustainable."
                    )
                )
            ),
            LearningCategory(
                id = "food",
                title = "Food",
                lessons = listOf(
                    LearningLesson(
                        id = "food_1",
                        categoryId = "food",
                        title = "Seasonal Eating",
                        shortDescription = "Seasonal food is fresher and more sustainable.",
                        imageAsset = "learning/food-lesson-1.jpg",
                        introText = "Eating seasonal food reduces environmental impact and improves nutrition.",
                        sectionOneTitle = "Why Seasonal Food Matters",
                        sectionOneBody = "Seasonal food is grown at the right time of year. It requires fewer artificial conditions like heating or cooling. This reduces energy use in agriculture. Seasonal products are often fresher and more nutritious. They travel shorter distances, reducing emissions. Supporting seasonal food helps local farmers. It also reduces the need for long-term storage. Natural growing cycles improve soil health. Seasonal diets bring more variety throughout the year. This approach supports sustainable food systems.",
                        sectionTwoTitle = "How to Eat Seasonally",
                        sectionTwoBody = "Learn what grows in your region each season. Visit local markets regularly. Choose fruits and vegetables that are in abundance. Try new recipes based on seasonal ingredients. Avoid imported out-of-season produce when possible. Plan meals around what is available. Store seasonal food properly. Freeze or preserve surplus. Support local agriculture programs. Make seasonal eating a habit.",
                        bottomImageAsset = "learning/food-lesson-1.jpg",
                        bottomCaption = "Eat with the seasons."
                    ),
                    LearningLesson(
                        id = "food_2",
                        categoryId = "food",
                        title = "Plant-Based Diet",
                        shortDescription = "More plants means lower emissions.",
                        imageAsset = "learning/food-lesson-2.jpg",
                        introText = "Plant-based meals are healthier and more sustainable.",
                        sectionOneTitle = "Environmental Impact",
                        sectionOneBody = "Plant-based foods require fewer resources. They use less water and land. They produce fewer greenhouse gas emissions. Reducing meat consumption lowers environmental pressure. Agriculture becomes more efficient. Biodiversity is better protected. Plant-based diets support sustainable farming. They also improve food security. Many plant foods are affordable. This makes them accessible to more people.",
                        sectionTwoTitle = "Getting Started",
                        sectionTwoBody = "Start with one plant-based meal per week. Replace meat with beans or lentils. Try plant-based recipes. Explore different cuisines. Use plant-based milk alternatives. Learn about protein sources. Gradually increase frequency. Plan balanced meals. Experiment with flavors. Build sustainable habits.",
                        bottomImageAsset = "learning/food-lesson-2.jpg",
                        bottomCaption = "More plants, less impact."
                    ),
                    LearningLesson(
                        id = "food_3",
                        categoryId = "food",
                        title = "Reducing Food Waste",
                        shortDescription = "Waste less and save resources.",
                        imageAsset = "learning/food-lesson-3.jpg",
                        introText = "Food waste is a major environmental issue.",
                        sectionOneTitle = "Why It Matters",
                        sectionOneBody = "Food waste wastes water and energy. It increases greenhouse gas emissions. Landfills produce methane from organic waste. Wasted food means wasted labor and resources. It contributes to global inefficiency. Reducing waste helps sustainability. It saves money for households. It improves food system efficiency. It reduces environmental impact. Small changes make a big difference.",
                        sectionTwoTitle = "How to Reduce Waste",
                        sectionTwoBody = "Plan meals in advance. Buy only what you need. Store food properly. Use leftovers creatively. Understand expiration labels. Freeze unused food. Cook smaller portions. Compost organic waste. Track what you throw away. Improve habits over time.",
                        bottomImageAsset = "learning/food-lesson-3.jpg",
                        bottomCaption = "Use what you buy."
                    ),
                    LearningLesson(
                        id = "food_4",
                        categoryId = "food",
                        title = "Local Food Choices",
                        shortDescription = "Local food reduces transport emissions.",
                        imageAsset = "learning/food-lesson-4.jpg",
                        introText = "Buying local supports communities.",
                        sectionOneTitle = "Benefits of Local Food",
                        sectionOneBody = "Local food travels shorter distances. This reduces transport emissions. It is often fresher and tastier. It supports local farmers. Money stays within the community. Seasonal alignment improves sustainability. Packaging is often reduced. Local systems are more transparent. Food security improves. Communities become stronger.",
                        sectionTwoTitle = "How to Buy Local",
                        sectionTwoBody = "Visit farmers markets. Join community-supported agriculture programs. Look for local labels in stores. Buy directly from producers. Choose seasonal local products. Support small businesses. Ask about product origins. Reduce reliance on imports. Build relationships with farmers. Make local choices regularly.",
                        bottomImageAsset = "learning/food-lesson-4.jpg",
                        bottomCaption = "Support local farms."
                    ),
                    LearningLesson(
                        id = "food_5",
                        categoryId = "food",
                        title = "Understanding Food Labels",
                        shortDescription = "Labels help make better food choices.",
                        imageAsset = "learning/food-lesson-5.jpg",
                        introText = "Food labels provide important information.",
                        sectionOneTitle = "Common Labels Explained",
                        sectionOneBody = "Organic labels indicate fewer chemicals. Fair trade supports ethical production. Eco labels show environmental standards. Labels help identify sustainable products. Certifications vary by region. Understanding them improves choices. Labels increase transparency. They support responsible companies. They guide consumers. Knowledge empowers decisions.",
                        sectionTwoTitle = "Using Labels Wisely",
                        sectionTwoBody = "Read labels carefully. Compare different products. Look for trusted certifications. Avoid misleading claims. Learn label meanings. Prioritize sustainability indicators. Combine label knowledge with research. Make informed purchases. Support responsible brands. Build awareness over time.",
                        bottomImageAsset = "learning/food-lesson-5.jpg",
                        bottomCaption = "Read before you buy."
                    ),
                    LearningLesson(
                        id = "food_6",
                        categoryId = "food",
                        title = "Sustainable Protein Sources",
                        shortDescription = "Choose proteins with lower environmental impact.",
                        imageAsset = "learning/food-lesson-6.jpg",
                        introText = "Protein choices affect sustainability.",
                        sectionOneTitle = "Better Protein Options",
                        sectionOneBody = "Plant proteins have lower emissions. Beans and lentils are efficient sources. Nuts and seeds provide healthy fats. Fish can be sustainable if sourced responsibly. Reducing red meat lowers impact. Protein diversity improves diet. Sustainable sources protect ecosystems. Balanced diets are important. Protein needs can be met easily. Smart choices make a difference.",
                        sectionTwoTitle = "Practical Tips",
                        sectionTwoBody = "Include legumes in meals. Try tofu or plant alternatives. Mix protein sources. Reduce portion sizes of meat. Explore vegetarian dishes. Plan balanced meals. Learn recipes. Check sourcing of fish. Experiment with new foods. Maintain variety.",
                        bottomImageAsset = "learning/food-lesson-6.jpg",
                        bottomCaption = "Choose smarter protein."
                    ),
                    LearningLesson(
                        id = "food_7",
                        categoryId = "food",
                        title = "Water Footprint of Food",
                        shortDescription = "Food production uses large amounts of water.",
                        imageAsset = "learning/food-lesson-7.jpg",
                        introText = "Every food has a hidden water cost.",
                        sectionOneTitle = "Understanding Water Use",
                        sectionOneBody = "Agriculture uses most freshwater globally. Meat production requires large water amounts. Plant foods usually require less. Water scarcity is a global issue. Efficient water use is critical. Food choices affect water demand. Sustainable farming reduces waste. Awareness helps conservation. Water footprint varies by product. Responsible choices matter.",
                        sectionTwoTitle = "Reducing Water Impact",
                        sectionTwoBody = "Eat more plant-based foods. Avoid food waste. Choose seasonal products. Support sustainable farming. Reduce overconsumption. Learn water footprints. Prefer efficient crops. Make conscious choices. Save water indirectly. Build sustainable habits.",
                        bottomImageAsset = "learning/food-lesson-7.jpg",
                        bottomCaption = "Save water through food."
                    ),
                    LearningLesson(
                        id = "food_8",
                        categoryId = "food",
                        title = "Packaging and Food",
                        shortDescription = "Food packaging creates waste.",
                        imageAsset = "learning/food-lesson-8.jpg",
                        introText = "Reducing packaging is key to sustainability.",
                        sectionOneTitle = "Why Packaging Matters",
                        sectionOneBody = "Packaging generates large waste volumes. Plastic is especially problematic. It pollutes land and oceans. Recycling rates are limited. Packaging uses energy and materials. Reducing it lowers impact. Bulk buying reduces waste. Reusable containers help. Awareness is growing. Change starts with consumers.",
                        sectionTwoTitle = "How to Reduce It",
                        sectionTwoBody = "Choose minimal packaging products. Buy in bulk. Use reusable bags and containers. Avoid single-use plastics. Support eco-friendly brands. Recycle properly. Plan purchases. Reduce unnecessary packaging. Bring your own containers. Build better habits.",
                        bottomImageAsset = "learning/food-lesson-8.jpg",
                        bottomCaption = "Less packaging, less waste."
                    ),
                    LearningLesson(
                        id = "food_9",
                        categoryId = "food",
                        title = "Cooking at Home",
                        shortDescription = "Home cooking reduces waste and emissions.",
                        imageAsset = "learning/food-lesson-9.jpg",
                        introText = "Cooking at home supports sustainable eating.",
                        sectionOneTitle = "Benefits of Home Cooking",
                        sectionOneBody = "Home cooking reduces packaging waste. It allows better portion control. It saves money. It improves nutrition. Ingredients can be sourced sustainably. It reduces food waste. Cooking skills improve over time. Meals become healthier. It reduces reliance on processed foods. It supports conscious eating.",
                        sectionTwoTitle = "How to Start",
                        sectionTwoBody = "Plan weekly meals. Prepare ingredients in advance. Learn simple recipes. Cook in batches. Store leftovers properly. Use seasonal ingredients. Reduce processed foods. Try new dishes. Make cooking enjoyable. Build consistency.",
                        bottomImageAsset = "learning/food-lesson-9.jpg",
                        bottomCaption = "Cook more, waste less."
                    ),
                    LearningLesson(
                        id = "food_10",
                        categoryId = "food",
                        title = "Future of Sustainable Food",
                        shortDescription = "Food systems are evolving.",
                        imageAsset = "learning/food-lesson-10.jpg",
                        introText = "Innovation is shaping the future of food.",
                        sectionOneTitle = "Emerging Trends",
                        sectionOneBody = "Plant-based alternatives are growing. Lab-grown meat is developing. Vertical farming is expanding. Technology improves efficiency. Food systems are becoming smarter. Sustainability drives innovation. Supply chains are evolving. Awareness is increasing. Consumers influence change. The future is more sustainable.",
                        sectionTwoTitle = "Your Role",
                        sectionTwoBody = "Stay informed about food trends. Make conscious choices. Support sustainable brands. Reduce waste. Choose eco-friendly products. Educate others. Adapt habits gradually. Try new foods. Stay curious. Contribute to change.",
                        bottomImageAsset = "learning/food-lesson-10.jpg",
                        bottomCaption = "The future of food is sustainable."
                    )
                )
            ),
            LearningCategory(
                id = "home",
                title = "Home",
                lessons = listOf(
                    LearningLesson(
                        id = "home_1",
                        categoryId = "home",
                        title = "Solar Energy",
                        shortDescription = "Clean energy from the sun.",
                        imageAsset = "learning/home-lesson-1.jpg",
                        introText = "Solar power reduces reliance on fossil fuels.",
                        sectionOneTitle = "How It Works",
                        sectionOneBody = "Panels convert sunlight into electricity.",
                        sectionTwoTitle = "Benefits",
                        sectionTwoBody = "Lower bills and emissions.",
                        bottomImageAsset = "learning/home-lesson-1.jpg",
                        bottomCaption = "Clean energy at home."
                    ),
                    LearningLesson(
                        id = "home_2",
                        categoryId = "home",
                        title = "Efficient Appliances",
                        shortDescription = "Save energy with better devices.",
                        imageAsset = "learning/home-lesson-2.jpg",
                        introText = "Modern appliances use less power.",
                        sectionOneTitle = "Why Upgrade",
                        sectionOneBody = "Reduces energy consumption.",
                        sectionTwoTitle = "Examples",
                        sectionTwoBody = "LED lights and efficient machines.",
                        bottomImageAsset = "learning/home-lesson-2.jpg",
                        bottomCaption = "Efficiency saves energy."
                    ),
                    LearningLesson(
                        id = "home_3",
                        categoryId = "home",
                        title = "Home Insulation",
                        shortDescription = "Keep heat in, save energy.",
                        imageAsset = "learning/home-lesson-3.jpg",
                        introText = "Insulation reduces heating needs.",
                        sectionOneTitle = "Energy Saving",
                        sectionOneBody = "Less heat loss means lower bills.",
                        sectionTwoTitle = "Improvements",
                        sectionTwoBody = "Seal windows and doors.",
                        bottomImageAsset = "learning/home-lesson-3.jpg",
                        bottomCaption = "Stay warm efficiently."
                    ),
                    LearningLesson(
                        id = "home_4",
                        categoryId = "home",
                        title = "Water Saving at Home",
                        shortDescription = "Reduce water use daily.",
                        imageAsset = "learning/home-lesson-4.jpg",
                        introText = "Water conservation is essential.",
                        sectionOneTitle = "Why Save Water",
                        sectionOneBody = "Protects resources and energy.",
                        sectionTwoTitle = "Tips",
                        sectionTwoBody = "Shorter showers and fixing leaks.",
                        bottomImageAsset = "learning/home-lesson-4.jpg",
                        bottomCaption = "Every drop counts."
                    )
                )
            ),
            LearningCategory(
                id = "purchases",
                title = "Purchases",
                lessons = listOf(
                    LearningLesson(
                        id = "purchases_1",
                        categoryId = "purchases",
                        title = "Reusable Products",
                        shortDescription = "Reduce waste with reusable items.",
                        imageAsset = "learning/purchases-lesson-1.jpg",
                        introText = "Reusable items lower environmental impact.",
                        sectionOneTitle = "Benefits",
                        sectionOneBody = "Less waste and long-term savings.",
                        sectionTwoTitle = "Examples",
                        sectionTwoBody = "Bottles, bags, containers.",
                        bottomImageAsset = "learning/purchases-lesson-1.jpg",
                        bottomCaption = "Reuse instead of waste."
                    ),
                    LearningLesson(
                        id = "purchases_2",
                        categoryId = "purchases",
                        title = "Buy Better",
                        shortDescription = "Choose quality over quantity.",
                        imageAsset = "learning/purchases-lesson-2.jpg",
                        introText = "Sustainable shopping starts with choices.",
                        sectionOneTitle = "Problem",
                        sectionOneBody = "Fast consumption creates waste.",
                        sectionTwoTitle = "Solution",
                        sectionTwoBody = "Buy durable products.",
                        bottomImageAsset = "learning/purchases-lesson-2.jpg",
                        bottomCaption = "Think before buying."
                    ),
                    LearningLesson(
                        id = "purchases_3",
                        categoryId = "purchases",
                        title = "Second-Hand Shopping",
                        shortDescription = "Extend product life.",
                        imageAsset = "learning/purchases-lesson-3.jpg",
                        introText = "Used items reduce demand for new ones.",
                        sectionOneTitle = "Benefits",
                        sectionOneBody = "Less waste and cheaper.",
                        sectionTwoTitle = "Where to Find",
                        sectionTwoBody = "Thrift stores and online.",
                        bottomImageAsset = "learning/purchases-lesson-3.jpg",
                        bottomCaption = "Reuse and save."
                    ),
                    LearningLesson(
                        id = "purchases_4",
                        categoryId = "purchases",
                        title = "Eco Labels and Certifications",
                        shortDescription = "Understand sustainable labels.",
                        imageAsset = "learning/purchases-lesson-4.jpg",
                        introText = "Labels help identify eco-friendly products.",
                        sectionOneTitle = "Common Labels",
                        sectionOneBody = "Organic, fair trade, eco-certified.",
                        sectionTwoTitle = "Why It Matters",
                        sectionTwoBody = "Helps make informed choices.",
                        bottomImageAsset = "learning/purchases-lesson-4.jpg",
                        bottomCaption = "Choose responsibly."
                    )
                )
            ),
            LearningCategory(
                id = "trees",
                title = "Trees",
                lessons = listOf(
                    LearningLesson(
                        id = "trees_1",
                        categoryId = "trees",
                        title = "Why Trees Matter",
                        shortDescription = "Trees are vital for life on Earth.",
                        imageAsset = "learning/trees-lesson-1.jpg",
                        introText = "Trees support ecosystems and climate balance.",
                        sectionOneTitle = "Environmental Role",
                        sectionOneBody = "They absorb CO2 and produce oxygen.",
                        sectionTwoTitle = "Biodiversity",
                        sectionTwoBody = "Provide habitat for wildlife.",
                        bottomImageAsset = "learning/trees-lesson-1.jpg",
                        bottomCaption = "Trees are life."
                    ),
                    LearningLesson(
                        id = "trees_2",
                        categoryId = "trees",
                        title = "Planting Trees",
                        shortDescription = "Tree planting helps fight climate change.",
                        imageAsset = "learning/trees-lesson-2.jpg",
                        introText = "Planting trees is a simple positive action.",
                        sectionOneTitle = "Benefits",
                        sectionOneBody = "Improves air and soil quality.",
                        sectionTwoTitle = "How to Start",
                        sectionTwoBody = "Join local planting programs.",
                        bottomImageAsset = "learning/trees-lesson-2.jpg",
                        bottomCaption = "Plant today for tomorrow."
                    ),
                    LearningLesson(
                        id = "trees_3",
                        categoryId = "trees",
                        title = "Protecting Forests",
                        shortDescription = "Forests need protection from deforestation.",
                        imageAsset = "learning/trees-lesson-3.jpg",
                        introText = "Forests are under threat worldwide.",
                        sectionOneTitle = "Threats",
                        sectionOneBody = "Logging and land clearing.",
                        sectionTwoTitle = "Solutions",
                        sectionTwoBody = "Conservation and awareness.",
                        bottomImageAsset = "learning/trees-lesson-3.jpg",
                        bottomCaption = "Protect our forests."
                    ),
                    LearningLesson(
                        id = "trees_4",
                        categoryId = "trees",
                        title = "Urban Green Spaces",
                        shortDescription = "Trees improve city life.",
                        imageAsset = "learning/trees-lesson-4.jpg",
                        introText = "Green spaces make cities healthier.",
                        sectionOneTitle = "Benefits",
                        sectionOneBody = "Cleaner air and cooler temperatures.",
                        sectionTwoTitle = "Community Impact",
                        sectionTwoBody = "Better well-being and recreation.",
                        bottomImageAsset = "learning/trees-lesson-4.jpg",
                        bottomCaption = "Nature in the city."
                    )
                )
            )
        )
    }

    fun getCategory(categoryId: String): LearningCategory? {
        return getCategories().firstOrNull { it.id == categoryId }
    }

    fun getLesson(lessonId: String): LearningLesson? {
        return getCategories().flatMap { it.lessons }.firstOrNull { it.id == lessonId }
    }
}

object LearningProgressStore {
    private const val PREFS_NAME = "learning_progress"
    private const val KEY_COMPLETED_IDS = "completed_ids"

    fun isCompleted(context: Context, lessonId: String): Boolean {
        return getCompletedIds(context).contains(lessonId)
    }

    fun markCompleted(context: Context, lessonId: String) {
        val updated = getCompletedIds(context).toMutableSet().apply { add(lessonId) }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_COMPLETED_IDS, updated)
            .apply()
        FirebaseSync.syncLearningProgress(context)
    }

    fun completedCount(context: Context, category: LearningCategory): Int {
        val completed = getCompletedIds(context)
        return category.lessons.count { completed.contains(it.id) }
    }

    fun getCategoryCompleted(context: Context, categoryId: String): Int {
        val category = LearningRepository.getCategory(categoryId) ?: return 0
        return completedCount(context, category)
    }

    fun getTotalCompleted(context: Context): Int = getCompletedIds(context).size

    fun getCompletedIds(context: Context): Set<String> = readCompletedIds(context)

    fun setCompletedIds(context: Context, ids: Set<String>) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_COMPLETED_IDS, ids)
            .apply()
    }

    private fun readCompletedIds(context: Context): Set<String> {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getStringSet(KEY_COMPLETED_IDS, emptySet())
            ?.toSet()
            ?: emptySet()
    }
}
