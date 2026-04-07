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
                        introText = "Public transport is one of the most effective tools for building sustainable cities. It helps move large numbers of people with less fuel and road space than private cars. Strong public transit systems reduce congestion and improve air quality. They also make cities more accessible for work, school, and daily life. Choosing shared transport supports cleaner and more efficient urban mobility.",
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
                        introText = "Carpooling is a simple and effective sustainability habit. It turns one trip into a shared journey instead of multiple separate drives. This reduces fuel use, traffic pressure, and per-person emissions. It can also lower commuting costs and make travel more social. Even a few shared trips each week can create measurable impact over time.",
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
                        introText = "Walking is often overlooked but highly effective. It produces no direct emissions and requires no fuel, parking, or special infrastructure beyond safe paths. For short trips, it can be one of the fastest and most practical options. Walking also improves physical health and helps reduce everyday stress. Making room for walking in daily routines supports both personal well-being and cleaner cities.",
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
                        introText = "Cycling combines efficiency with sustainability. It allows people to travel quickly through cities without producing direct emissions. Regular cycling improves fitness and reduces transport costs at the same time. As bike infrastructure expands, cycling becomes safer and more convenient for everyday use. It is one of the clearest examples of a transport choice that benefits both people and the planet.",
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
                        introText = "Electric vehicles are transforming transport systems. They offer a lower-emission alternative to conventional petrol and diesel cars, especially when charged with cleaner electricity. EVs also reduce noise pollution and can be cheaper to maintain over time. As charging networks improve, they are becoming more practical for more drivers. Their growth is an important part of the wider transition to cleaner mobility.",
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
                        introText = "Air travel is one of the largest contributors to personal emissions. A single flight can create far more carbon impact than many everyday transport choices on the ground. Because of this, reducing unnecessary flights can make a meaningful difference. Smarter planning and better alternatives can lower the impact without eliminating travel completely. Understanding aviation emissions is the first step toward more responsible decisions.",
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
                        introText = "Planning routes can reduce environmental impact. Better planning helps avoid traffic, shorten travel time, and cut unnecessary fuel use. It also makes it easier to combine errands into fewer journeys. Small improvements in route choices can reduce both stress and emissions. Efficient planning turns everyday travel into a more sustainable routine.",
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
                        introText = "Shared mobility is changing how we use transport. Bike sharing, car sharing, and app-based access reduce the need for personal vehicle ownership. This can save money while using urban space more efficiently. Shared systems also encourage flexible choices based on what is needed for each trip. The result is a transport model built more around access than possession.",
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
                        introText = "Driving habits have a big impact on emissions. The way a vehicle is driven can change fuel use even when the route stays the same. Smooth acceleration, steady speeds, and good maintenance all improve efficiency. These small adjustments reduce both costs and environmental impact. Smarter driving is one of the fastest ways to lower transport emissions without changing vehicles.",
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
                        introText = "Transport is evolving toward sustainability. New technologies, cleaner energy, and smarter city planning are reshaping how people move. Electric mobility, better transit, and shared systems are becoming more common worldwide. At the same time, personal choices still matter in how quickly this transition happens. The future of mobility depends on both innovation and everyday behavior.",
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
                        introText = "Eating seasonal food reduces environmental impact and improves nutrition. Produce grown in its natural season usually needs less artificial heating, lighting, and storage. It also tends to travel shorter distances before reaching your plate. This often means fresher flavor and better quality. Choosing seasonal ingredients is a practical way to support more sustainable food systems.",
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
                        introText = "Plant-based meals are healthier and more sustainable. Foods such as beans, grains, vegetables, nuts, and seeds generally require fewer resources than many animal products. Adding more of them to your week can reduce emissions and water use. It also opens the door to more variety in everyday meals. Even gradual changes can have a strong long-term effect.",
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
                        introText = "Food waste is a major environmental issue. When food is thrown away, the water, energy, land, and labor used to produce it are wasted as well. This adds pressure across the entire food system and increases emissions from disposal. Reducing waste is one of the simplest ways to make home kitchens more sustainable. Better planning and storage can make a noticeable difference quickly.",
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
                        introText = "Buying local supports communities. Food produced closer to home often requires less transport and can reach consumers faster. This may reduce emissions while keeping more value within the local economy. Local systems can also offer greater transparency about where products come from. Supporting nearby producers strengthens both sustainability and resilience.",
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
                        introText = "Food labels provide important information. They can help people understand how products were grown, processed, and certified. Labels also make it easier to compare food choices beyond just price or appearance. When consumers understand them properly, they can reward more responsible production. Better label literacy leads to better daily decisions.",
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
                        introText = "Protein choices affect sustainability. Different protein sources have very different impacts on land use, water demand, and emissions. Exploring a wider mix of plant proteins and responsibly sourced options can lower environmental pressure. It can also improve dietary balance and meal variety. Smarter protein decisions are a practical part of sustainable eating.",
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
                        introText = "Every food has a hidden water cost. Water is used throughout growing, processing, and transporting what we eat. Some products require far more freshwater than others, especially when produced inefficiently. Understanding this hidden footprint helps connect diet choices with wider resource use. More informed eating can support long-term water conservation.",
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
                        introText = "Reducing packaging is key to sustainability. Food packaging consumes materials and energy before it ever becomes waste. When it is excessive or hard to recycle, the environmental burden grows quickly. Choosing products with less packaging helps reduce pollution and disposal pressure. Small purchasing habits can lead to a cleaner and more efficient food system.",
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
                        introText = "Cooking at home supports sustainable eating. It gives people more control over ingredients, portions, and food storage. Home-prepared meals often use less packaging and create fewer leftovers from oversized servings. They also make it easier to use seasonal, local, or lower-impact ingredients. Building cooking habits can improve both health and sustainability.",
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
                        introText = "Innovation is shaping the future of food. New farming methods, alternative proteins, and smarter supply chains are changing how food is produced and consumed. These advances aim to lower emissions, reduce waste, and improve resilience. Consumer choices help determine which solutions grow fastest. The future of food will be shaped by both technology and habits.",
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
                        shortDescription = "Generate clean energy from sunlight.",
                        imageAsset = "learning/home-lesson-1.jpg",
                        introText = "Solar energy is one of the most accessible renewable energy sources for households. It allows homes to generate electricity from a resource that is abundant and clean. Over time, solar systems can reduce dependence on carbon-intensive grids and lower energy bills. They also increase resilience by giving households more control over power use. Solar is a practical example of sustainability becoming part of daily living.",
                        sectionOneTitle = "Why Solar Matters",
                        sectionOneBody = "Solar energy reduces dependence on fossil fuels. It lowers greenhouse gas emissions. Solar panels convert sunlight into electricity efficiently. Over time, this reduces energy costs. It increases energy independence. Solar systems require minimal maintenance. They work even in moderate sunlight conditions. Governments often support solar adoption. Homes with solar increase in value. Solar energy is a long-term sustainable investment.",
                        sectionTwoTitle = "How to Get Started",
                        sectionTwoBody = "Evaluate your roof exposure to sunlight. Check local incentives and subsidies. Compare installation costs and benefits. Choose certified installers. Start with small systems if needed. Monitor energy usage. Combine with energy-efficient habits. Maintain panels regularly. Learn about battery storage options. Transition gradually to renewable energy.",
                        bottomImageAsset = "learning/home-lesson-1.jpg",
                        bottomCaption = "Power your home with the sun."
                    ),
                    LearningLesson(
                        id = "home_2",
                        categoryId = "home",
                        title = "Efficient Appliances",
                        shortDescription = "Reduce energy use with smart devices.",
                        imageAsset = "learning/home-lesson-2.jpg",
                        introText = "Appliances account for a large part of home energy consumption. Many households use far more electricity than they realize through everyday devices. Efficient models can deliver the same results while using much less power. Over time, these savings reduce both utility bills and environmental impact. Choosing smarter appliances is one of the easiest home upgrades to understand and measure.",
                        sectionOneTitle = "Why Efficiency Matters",
                        sectionOneBody = "Efficient appliances use less electricity. This reduces energy bills. It lowers environmental impact. Modern devices are designed to optimize performance. Energy labels help compare efficiency. Older appliances waste more power. Upgrading reduces long-term costs. Efficient homes are more sustainable. It supports energy conservation. Small upgrades create big impact.",
                        sectionTwoTitle = "Smart Upgrades",
                        sectionTwoBody = "Start with frequently used appliances. Replace old refrigerators or washers. Look for high energy ratings. Use eco modes when available. Turn off standby devices. Use smart plugs. Maintain appliances regularly. Avoid overuse. Monitor energy consumption. Upgrade gradually.",
                        bottomImageAsset = "learning/home-lesson-2.jpg",
                        bottomCaption = "Efficiency saves energy."
                    ),
                    LearningLesson(
                        id = "home_3",
                        categoryId = "home",
                        title = "Home Insulation",
                        shortDescription = "Keep heat inside and reduce energy waste.",
                        imageAsset = "learning/home-lesson-3.jpg",
                        introText = "Insulation is key to maintaining comfortable temperatures. Without it, homes lose heat in winter and absorb too much warmth in summer. That forces heating and cooling systems to work harder than necessary. Better insulation lowers energy demand while making indoor spaces more stable and comfortable. It is one of the strongest long-term improvements a home can make.",
                        sectionOneTitle = "Energy Saving Benefits",
                        sectionOneBody = "Insulation reduces heat loss in winter. It keeps cool air inside during summer. This lowers heating and cooling costs. It improves home comfort. Less energy is required overall. Proper insulation reduces emissions. It prevents drafts and leaks. Buildings become more efficient. It increases property value. Insulation is a long-term solution.",
                        sectionTwoTitle = "Where to Improve",
                        sectionTwoBody = "Check attic insulation first. Seal windows and doors. Insulate walls and floors. Use weather stripping. Upgrade windows if needed. Add thermal curtains. Inspect regularly. Fix leaks promptly. Use professional audits. Improve gradually.",
                        bottomImageAsset = "learning/home-lesson-3.jpg",
                        bottomCaption = "Keep warmth where it belongs."
                    ),
                    LearningLesson(
                        id = "home_4",
                        categoryId = "home",
                        title = "Water Saving",
                        shortDescription = "Reduce water consumption daily.",
                        imageAsset = "learning/home-lesson-4.jpg",
                        introText = "Water is a limited resource that must be used responsibly. Homes consume water every day for washing, cleaning, cooking, and outdoor use. Small inefficiencies add up quickly across months and years. Reducing waste protects natural systems while also lowering bills. Better water habits are simple, practical, and important in every household.",
                        sectionOneTitle = "Why Save Water",
                        sectionOneBody = "Freshwater resources are limited globally. Overuse leads to shortages. Water treatment requires energy. Saving water reduces environmental impact. It lowers utility bills. Efficient use protects ecosystems. Every drop matters. Awareness is increasing worldwide. Conservation is essential. Small habits help.",
                        sectionTwoTitle = "Simple Actions",
                        sectionTwoBody = "Take shorter showers. Fix leaks immediately. Turn off taps when not needed. Use efficient fixtures. Collect rainwater. Water plants wisely. Run full loads in machines. Monitor usage. Educate family members. Build consistent habits.",
                        bottomImageAsset = "learning/home-lesson-4.jpg",
                        bottomCaption = "Save water every day."
                    ),
                    LearningLesson(
                        id = "home_5",
                        categoryId = "home",
                        title = "Smart Thermostats",
                        shortDescription = "Control temperature efficiently.",
                        imageAsset = "learning/home-lesson-5.jpg",
                        introText = "Smart thermostats optimize heating and cooling automatically. They help match indoor temperatures to real daily routines instead of running at full intensity all the time. This reduces wasted energy while keeping homes comfortable. Many systems also provide data that helps households understand their consumption better. Smarter temperature control is a direct path to more efficient living.",
                        sectionOneTitle = "Benefits",
                        sectionOneBody = "Smart thermostats adjust temperature automatically. They reduce unnecessary energy use. They learn your habits over time. Remote control improves convenience. They help lower energy bills. Temperature control becomes precise. They integrate with smart homes. They reduce emissions. They improve comfort. Technology makes energy smarter.",
                        sectionTwoTitle = "How to Use",
                        sectionTwoBody = "Set schedules based on daily routines. Lower temperature when away. Use eco modes. Monitor energy reports. Adjust settings seasonally. Combine with insulation. Avoid extreme settings. Keep system maintained. Learn features. Optimize gradually.",
                        bottomImageAsset = "learning/home-lesson-5.jpg",
                        bottomCaption = "Control energy intelligently."
                    ),
                    LearningLesson(
                        id = "home_6",
                        categoryId = "home",
                        title = "Waste Reduction",
                        shortDescription = "Reduce household waste effectively.",
                        imageAsset = "learning/home-lesson-6.jpg",
                        introText = "Waste reduction is key to sustainable living. Every product thrown away represents materials, energy, and labor that were used only briefly. Homes generate a steady stream of waste through packaging, food scraps, and disposable items. Reducing that flow lowers environmental impact and often saves money too. Sustainable households focus first on preventing waste before managing it.",
                        sectionOneTitle = "Why It Matters",
                        sectionOneBody = "Waste pollutes land and oceans. It consumes resources unnecessarily. Recycling is not always enough. Reducing waste is more effective. It lowers environmental impact. It saves money. It supports circular economy. It reduces landfill use. It conserves materials. It promotes responsibility.",
                        sectionTwoTitle = "How to Reduce Waste",
                        sectionTwoBody = "Avoid single-use items. Use reusable products. Sort waste properly. Compost organic waste. Buy less packaging. Repair instead of replacing. Donate unused items. Plan purchases. Track waste. Improve habits.",
                        bottomImageAsset = "learning/home-lesson-6.jpg",
                        bottomCaption = "Reduce, reuse, recycle."
                    ),
                    LearningLesson(
                        id = "home_7",
                        categoryId = "home",
                        title = "Energy-Efficient Lighting",
                        shortDescription = "Use less energy with better lighting.",
                        imageAsset = "learning/home-lesson-7.jpg",
                        introText = "Lighting is a simple place to save energy. It is used throughout the home every day, which makes small efficiency gains add up fast. Modern bulbs provide better performance with lower electricity demand and longer life. This reduces both costs and replacement frequency. Better lighting choices are an easy starting point for broader home sustainability.",
                        sectionOneTitle = "Why It Matters",
                        sectionOneBody = "LED lights use less electricity. They last longer than traditional bulbs. They reduce energy bills. Lighting accounts for significant usage. Efficient bulbs reduce emissions. They are widely available. They improve brightness quality. They require less replacement. They are cost-effective. Switching is easy.",
                        sectionTwoTitle = "Easy Changes",
                        sectionTwoBody = "Replace old bulbs with LEDs. Use natural light when possible. Turn off lights when leaving rooms. Install motion sensors. Choose warm lighting. Use task lighting. Avoid over-lighting. Maintain fixtures. Optimize placement. Build habits.",
                        bottomImageAsset = "learning/home-lesson-7.jpg",
                        bottomCaption = "Light smarter."
                    ),
                    LearningLesson(
                        id = "home_8",
                        categoryId = "home",
                        title = "Natural Ventilation",
                        shortDescription = "Cool your home without extra energy.",
                        imageAsset = "learning/home-lesson-8.jpg",
                        introText = "Airflow can reduce reliance on air conditioning. When homes are ventilated well, they stay fresher and often cooler without as much mechanical cooling. Natural ventilation can improve comfort while lowering electricity demand. It also helps remove indoor pollutants and stale air. Good airflow design makes homes healthier and more efficient.",
                        sectionOneTitle = "Why Ventilation Matters",
                        sectionOneBody = "Natural airflow cools indoor spaces. It reduces energy use. Fresh air improves health. It removes indoor pollutants. It increases comfort. It lowers electricity demand. It works best with proper design. It supports sustainability. It reduces costs. It is simple to implement.",
                        sectionTwoTitle = "How to Improve Airflow",
                        sectionTwoBody = "Open windows strategically. Create cross ventilation. Use fans to assist airflow. Keep vents clear. Use shades to block heat. Ventilate at cooler times. Adjust furniture placement. Monitor airflow patterns. Improve gradually. Combine with other methods.",
                        bottomImageAsset = "learning/home-lesson-8.jpg",
                        bottomCaption = "Let air flow naturally."
                    ),
                    LearningLesson(
                        id = "home_9",
                        categoryId = "home",
                        title = "Rainwater Harvesting",
                        shortDescription = "Collect and reuse rainwater.",
                        imageAsset = "learning/home-lesson-9.jpg",
                        introText = "Rainwater can be reused for many household needs. Collecting it reduces pressure on treated freshwater supplies and makes better use of a natural resource. For gardening, cleaning, and similar tasks, rainwater can be both practical and efficient. It also helps manage runoff and reduce strain on drainage systems. Harvesting rainwater is a clear example of resource-conscious living.",
                        sectionOneTitle = "Benefits",
                        sectionOneBody = "Rainwater reduces demand on freshwater. It is useful for gardening. It lowers water bills. It reduces runoff pollution. Systems are easy to install. It supports sustainability. It conserves resources. It works in many climates. It reduces strain on infrastructure. It is practical and efficient.",
                        sectionTwoTitle = "How to Start",
                        sectionTwoBody = "Install collection barrels. Connect to gutters. Use filters if needed. Store safely. Use for plants or cleaning. Maintain system regularly. Prevent overflow. Check local regulations. Expand system gradually. Monitor usage.",
                        bottomImageAsset = "learning/home-lesson-9.jpg",
                        bottomCaption = "Use nature’s water."
                    ),
                    LearningLesson(
                        id = "home_10",
                        categoryId = "home",
                        title = "Sustainable Home Design",
                        shortDescription = "Design homes with sustainability in mind.",
                        imageAsset = "learning/home-lesson-10.jpg",
                        introText = "Sustainable design improves efficiency and comfort. The way a home is planned shapes its energy use, indoor climate, and long-term environmental footprint. Better materials, smarter layouts, and natural light all contribute to lower waste and stronger performance. Good design can reduce costs while improving daily quality of life. A sustainable home works better because it is designed with purpose.",
                        sectionOneTitle = "Core Principles",
                        sectionOneBody = "Sustainable homes use less energy. They maximize natural light. They use eco-friendly materials. They reduce waste. They improve air quality. They integrate renewable energy. They optimize insulation. They support long-term savings. They reduce environmental impact. They improve quality of life.",
                        sectionTwoTitle = "Applying It",
                        sectionTwoBody = "Use sustainable materials. Design for sunlight. Improve insulation. Install efficient systems. Reduce water use. Use renewable energy. Plan smart layouts. Minimize waste. Upgrade gradually. Think long-term.",
                        bottomImageAsset = "learning/home-lesson-10.jpg",
                        bottomCaption = "Design for the future."
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
                        shortDescription = "Replace disposable items with reusable ones.",
                        imageAsset = "learning/purchases-lesson-1.jpg",
                        introText = "Reusable products help reduce waste and save resources. Items designed for repeated use replace a constant stream of disposable alternatives. This lowers demand for raw materials, packaging, and frequent repurchasing. Over time, reusables can save money while reducing environmental harm. They make sustainability part of normal daily habits rather than a separate effort.",
                        sectionOneTitle = "Why Reusables Matter",
                        sectionOneBody = "Single-use products create large amounts of waste. They require constant production and disposal. Reusable items reduce resource consumption. They last longer and are more cost-effective. Plastic pollution is reduced significantly. Reusables support sustainable habits. They decrease landfill waste. They reduce environmental impact. Many reusable options are widely available. Small changes lead to big results.",
                        sectionTwoTitle = "How to Switch",
                        sectionTwoBody = "Start with reusable bags and bottles. Replace disposable cups with travel mugs. Use reusable containers for food. Carry reusable cutlery. Avoid single-use plastics. Choose durable products. Keep items accessible daily. Build consistent habits. Encourage others to join. Make reusables part of your lifestyle.",
                        bottomImageAsset = "learning/purchases-lesson-1.jpg",
                        bottomCaption = "Reuse instead of waste."
                    ),
                    LearningLesson(
                        id = "purchases_2",
                        categoryId = "purchases",
                        title = "Buy Less, Choose Better",
                        shortDescription = "Focus on quality over quantity.",
                        imageAsset = "learning/purchases-lesson-2.jpg",
                        introText = "Reducing consumption is one of the most effective sustainability actions. Many environmental impacts begin long before a product is used, during extraction, manufacturing, and transport. Buying fewer things reduces pressure across that whole chain. It also encourages people to focus on quality, durability, and real need. Conscious purchasing is often more powerful than simply choosing a greener version of the same excess.",
                        sectionOneTitle = "The Problem with Overconsumption",
                        sectionOneBody = "Fast consumption leads to increased waste. Cheap products often have short lifespans. This creates a cycle of constant replacement. Production consumes energy and resources. Waste accumulates quickly. Environmental impact increases. Quality products last longer. Fewer purchases reduce waste. Conscious buying is more sustainable. Less is often more.",
                        sectionTwoTitle = "How to Buy Smarter",
                        sectionTwoBody = "Ask if you really need the item. Choose durable products. Research before buying. Avoid impulse purchases. Prefer timeless designs. Invest in quality. Repair instead of replacing. Reduce unnecessary spending. Plan purchases carefully. Build mindful habits.",
                        bottomImageAsset = "learning/purchases-lesson-2.jpg",
                        bottomCaption = "Quality over quantity."
                    ),
                    LearningLesson(
                        id = "purchases_3",
                        categoryId = "purchases",
                        title = "Second-Hand Shopping",
                        shortDescription = "Extend product life through reuse.",
                        imageAsset = "learning/purchases-lesson-3.jpg",
                        introText = "Buying used items reduces demand for new production. It keeps existing products in circulation longer and lowers the need for new materials and manufacturing. Second-hand shopping can also make sustainable choices more affordable and accessible. Many items still have long useful lives even after their first owner. Reuse is a practical way to cut waste without sacrificing quality.",
                        sectionOneTitle = "Why Second-Hand Matters",
                        sectionOneBody = "Second-hand products reduce waste. They extend product lifecycles. Fewer resources are used. Production demand decreases. It is often more affordable. It supports circular economy. Items stay in use longer. It reduces environmental impact. It encourages sustainable behavior. It benefits both buyers and the planet.",
                        sectionTwoTitle = "How to Start",
                        sectionTwoBody = "Explore thrift stores. Use online marketplaces. Check quality before buying. Look for durable items. Buy what you need. Sell unused items. Donate instead of discarding. Support local reuse communities. Be open to pre-owned goods. Make it a habit.",
                        bottomImageAsset = "learning/purchases-lesson-3.jpg",
                        bottomCaption = "Give products a second life."
                    ),
                    LearningLesson(
                        id = "purchases_4",
                        categoryId = "purchases",
                        title = "Eco Labels and Certifications",
                        shortDescription = "Understand what sustainable labels mean.",
                        imageAsset = "learning/purchases-lesson-4.jpg",
                        introText = "Labels help identify environmentally responsible products. They can point to standards around materials, labor, sourcing, or emissions. For shoppers, they create a quicker way to evaluate products beyond marketing claims alone. When understood properly, labels make it easier to reward better practices. Clear information leads to stronger purchasing decisions.",
                        sectionOneTitle = "Common Labels",
                        sectionOneBody = "Eco labels indicate sustainability standards. Organic labels reduce chemical use. Fair trade supports ethical labor. Certifications vary by region. Labels improve transparency. They guide better choices. Not all labels are equal. Understanding them is important. They promote responsible production. Consumers benefit from awareness.",
                        sectionTwoTitle = "How to Use Them",
                        sectionTwoBody = "Read product labels carefully. Look for trusted certifications. Avoid misleading claims. Compare products. Learn label meanings. Research brands. Prioritize sustainable options. Stay informed. Choose responsibly. Build knowledge over time.",
                        bottomImageAsset = "learning/purchases-lesson-4.jpg",
                        bottomCaption = "Know what you buy."
                    ),
                    LearningLesson(
                        id = "purchases_5",
                        categoryId = "purchases",
                        title = "Minimalist Lifestyle",
                        shortDescription = "Own less and live more consciously.",
                        imageAsset = "learning/purchases-lesson-5.jpg",
                        introText = "Minimalism reduces consumption and increases clarity. By focusing on what is useful and meaningful, people often buy less and waste less. This lowers environmental impact while also simplifying spaces and routines. Minimalism is not about deprivation, but about intentional choices. It aligns everyday living with long-term sustainability.",
                        sectionOneTitle = "Why Minimalism Works",
                        sectionOneBody = "Owning fewer items reduces waste. It lowers environmental impact. It simplifies daily life. It saves money. It reduces stress. It encourages mindful decisions. It supports sustainability. It reduces clutter. It focuses on essentials. It improves well-being.",
                        sectionTwoTitle = "How to Apply It",
                        sectionTwoBody = "Declutter regularly. Keep only necessary items. Avoid impulse buying. Focus on quality. Donate unused items. Reduce storage needs. Simplify routines. Practice mindful consumption. Reflect before purchasing. Build intentional habits.",
                        bottomImageAsset = "learning/purchases-lesson-5.jpg",
                        bottomCaption = "Less stuff, more life."
                    ),
                    LearningLesson(
                        id = "purchases_6",
                        categoryId = "purchases",
                        title = "Sustainable Fashion",
                        shortDescription = "Choose clothing with lower impact.",
                        imageAsset = "learning/purchases-lesson-6.jpg",
                        introText = "Fashion has a large environmental footprint. Clothing production uses water, energy, chemicals, and transport at a massive scale. Fast-changing trends often shorten the lifespan of garments and increase waste. More thoughtful choices can reduce pressure on both ecosystems and workers. A sustainable wardrobe starts with slower, better decisions.",
                        sectionOneTitle = "Why It Matters",
                        sectionOneBody = "Fast fashion creates waste. It uses large amounts of water. It generates pollution. Clothing lifecycles are short. Sustainable fashion reduces impact. It promotes ethical production. It supports better materials. It encourages longer use. It reduces waste. It improves industry practices.",
                        sectionTwoTitle = "Better Choices",
                        sectionTwoBody = "Buy fewer clothes. Choose quality fabrics. Support ethical brands. Repair garments. Buy second-hand. Avoid fast trends. Care for clothing properly. Wash less frequently. Reuse creatively. Build a sustainable wardrobe.",
                        bottomImageAsset = "learning/purchases-lesson-6.jpg",
                        bottomCaption = "Wear responsibly."
                    ),
                    LearningLesson(
                        id = "purchases_7",
                        categoryId = "purchases",
                        title = "Packaging Awareness",
                        shortDescription = "Reduce waste from product packaging.",
                        imageAsset = "learning/purchases-lesson-7.jpg",
                        introText = "Packaging contributes heavily to pollution. Much of it is used briefly and then discarded, even though it required materials and energy to produce. When packaging is excessive or difficult to recycle, the impact grows even more. Consumers can influence this by choosing products with simpler, smarter formats. Better packaging habits reduce waste across the whole purchasing cycle.",
                        sectionOneTitle = "The Impact",
                        sectionOneBody = "Packaging generates significant waste. Plastic is difficult to recycle. It pollutes ecosystems. It uses resources to produce. Reducing packaging lowers impact. Bulk buying helps. Reusable packaging is better. Awareness is growing. Consumer demand drives change. Packaging choices matter.",
                        sectionTwoTitle = "What You Can Do",
                        sectionTwoBody = "Choose minimal packaging. Buy in bulk. Use reusable containers. Avoid plastic wrapping. Recycle properly. Support eco-friendly brands. Bring your own bags. Reduce unnecessary purchases. Plan ahead. Build habits.",
                        bottomImageAsset = "learning/purchases-lesson-7.jpg",
                        bottomCaption = "Less packaging, less waste."
                    ),
                    LearningLesson(
                        id = "purchases_8",
                        categoryId = "purchases",
                        title = "Repair Instead of Replace",
                        shortDescription = "Extend product life through repair.",
                        imageAsset = "learning/purchases-lesson-8.jpg",
                        introText = "Repairing items reduces waste and saves resources. Instead of replacing a product at the first sign of damage, repair extends its useful life and delays disposal. This lowers demand for new materials and manufacturing. It can also save money and rebuild practical skills. A repair mindset supports a more circular and responsible economy.",
                        sectionOneTitle = "Why Repair Matters",
                        sectionOneBody = "Repairing reduces waste. It extends product lifespan. It saves money. It reduces resource use. It supports sustainability. It encourages responsibility. It reduces landfill impact. It builds useful skills. It supports local repair services. It promotes circular economy.",
                        sectionTwoTitle = "How to Start",
                        sectionTwoBody = "Learn basic repair skills. Fix small issues early. Use repair guides. Support repair shops. Replace parts instead of products. Maintain items regularly. Share tools. Encourage repair culture. Avoid unnecessary disposal. Build long-term habits.",
                        bottomImageAsset = "learning/purchases-lesson-8.jpg",
                        bottomCaption = "Fix, don’t throw."
                    ),
                    LearningLesson(
                        id = "purchases_9",
                        categoryId = "purchases",
                        title = "Digital Consumption",
                        shortDescription = "Reduce impact of digital purchases.",
                        imageAsset = "learning/purchases-lesson-9.jpg",
                        introText = "Digital services also have environmental costs. Streaming, storage, cloud services, and online shopping all rely on physical infrastructure that uses energy and materials. Because these impacts are less visible, they are easy to overlook. More conscious digital habits can reduce unnecessary demand and extend device life. Sustainable consumption includes the digital world too.",
                        sectionOneTitle = "Hidden Impact",
                        sectionOneBody = "Data centers consume energy. Streaming uses electricity. Digital storage has a footprint. Devices require resources. Online shopping creates logistics emissions. Awareness is important. Reducing usage helps. Efficient habits matter. Digital impact is often overlooked. Conscious use is needed.",
                        sectionTwoTitle = "Smarter Use",
                        sectionTwoBody = "Reduce unnecessary streaming. Delete unused files. Optimize device usage. Extend device lifespan. Limit impulse online shopping. Use energy-efficient settings. Choose sustainable services. Monitor usage. Stay informed. Build better habits.",
                        bottomImageAsset = "learning/purchases-lesson-9.jpg",
                        bottomCaption = "Digital also matters."
                    ),
                    LearningLesson(
                        id = "purchases_10",
                        categoryId = "purchases",
                        title = "Circular Economy",
                        shortDescription = "Keep resources in use longer.",
                        imageAsset = "learning/purchases-lesson-10.jpg",
                        introText = "The circular economy reduces waste and maximizes resources. Instead of treating products as disposable, it aims to keep materials in use for as long as possible. Repair, reuse, recycling, and better design all play a role in this model. It reduces pressure on raw resource extraction and landfill use. Circular thinking changes how products are made, used, and valued.",
                        sectionOneTitle = "Core Idea",
                        sectionOneBody = "Products are reused and recycled. Waste is minimized. Materials stay in circulation. Production becomes more efficient. It reduces environmental impact. It supports sustainability. It changes consumption habits. It promotes repair and reuse. It benefits economies. It is the future of consumption.",
                        sectionTwoTitle = "Your Role",
                        sectionTwoBody = "Buy durable products. Reuse and recycle. Support circular brands. Repair items. Reduce waste. Share resources. Educate others. Make conscious choices. Build sustainable habits. Contribute to change.",
                        bottomImageAsset = "learning/purchases-lesson-10.jpg",
                        bottomCaption = "Close the loop."
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
                        shortDescription = "Trees are essential for life on Earth.",
                        imageAsset = "learning/trees-lesson-1.jpg",
                        introText = "Trees play a critical role in maintaining ecological balance. They connect climate, soil, water, and biodiversity in ways that support life across entire ecosystems. Their presence shapes healthier landscapes for both wildlife and people. When tree cover is lost, many systems weaken at once. Understanding their role helps explain why protecting them matters so much.",
                        sectionOneTitle = "Environmental Role",
                        sectionOneBody = "Trees absorb carbon dioxide from the atmosphere. This helps reduce global warming. They release oxygen necessary for life. Trees regulate temperature and humidity. They prevent soil erosion. They improve air quality. Forests support biodiversity. They provide habitats for many species. Trees help maintain water cycles. They are essential for a healthy planet.",
                        sectionTwoTitle = "Human Benefits",
                        sectionTwoBody = "Trees provide shade and cooling. They reduce urban heat. They improve mental well-being. Green spaces increase quality of life. Trees provide resources like fruits and wood. They support agriculture. They enhance landscapes. They reduce noise pollution. They increase property value. Trees benefit both people and nature.",
                        bottomImageAsset = "learning/trees-lesson-1.jpg",
                        bottomCaption = "Trees sustain life."
                    ),
                    LearningLesson(
                        id = "trees_2",
                        categoryId = "trees",
                        title = "Planting Trees",
                        shortDescription = "Planting trees helps restore ecosystems.",
                        imageAsset = "learning/trees-lesson-2.jpg",
                        introText = "Tree planting is one of the simplest environmental actions. A single tree can provide benefits for decades when planted in the right place and cared for properly. Planting supports restoration, biodiversity, and long-term carbon storage. It also creates visible local change that communities can rally around. Small planting efforts can become part of much bigger environmental recovery.",
                        sectionOneTitle = "Why Plant Trees",
                        sectionOneBody = "Planting trees absorbs carbon dioxide. It restores degraded land. It supports biodiversity. It improves soil quality. It increases green cover. It helps regulate climate. It supports wildlife habitats. It enhances landscapes. It is a long-term investment. It contributes to sustainability.",
                        sectionTwoTitle = "How to Start",
                        sectionTwoBody = "Choose native species. Plant in suitable locations. Water regularly at early stages. Protect young trees. Join community programs. Learn planting techniques. Monitor growth. Avoid invasive species. Support reforestation projects. Make it a regular activity.",
                        bottomImageAsset = "learning/trees-lesson-2.jpg",
                        bottomCaption = "Plant for the future."
                    ),
                    LearningLesson(
                        id = "trees_3",
                        categoryId = "trees",
                        title = "Deforestation Impact",
                        shortDescription = "Forest loss harms the planet.",
                        imageAsset = "learning/trees-lesson-3.jpg",
                        introText = "Deforestation is one of the biggest environmental threats. When forests are cleared, stored carbon is released and habitats disappear rapidly. This weakens biodiversity, disrupts water cycles, and damages local communities. The effects are often long-lasting and difficult to reverse. Understanding forest loss is essential for making better consumption and policy choices.",
                        sectionOneTitle = "What Happens",
                        sectionOneBody = "Deforestation releases stored carbon. It increases global warming. It destroys habitats. Biodiversity declines rapidly. Soil becomes degraded. Water cycles are disrupted. Ecosystems collapse. Indigenous communities are affected. Land becomes less productive. Long-term damage occurs.",
                        sectionTwoTitle = "How to Help",
                        sectionTwoBody = "Reduce paper use. Support sustainable products. Avoid illegal logging products. Raise awareness. Support conservation organizations. Choose eco-certified goods. Reduce consumption. Advocate for policies. Educate others. Make responsible choices.",
                        bottomImageAsset = "learning/trees-lesson-3.jpg",
                        bottomCaption = "Protect forests."
                    ),
                    LearningLesson(
                        id = "trees_4",
                        categoryId = "trees",
                        title = "Urban Trees",
                        shortDescription = "Trees improve city environments.",
                        imageAsset = "learning/trees-lesson-4.jpg",
                        introText = "Urban trees make cities healthier and more livable. They cool streets, improve air quality, and soften the harshness of built environments. Their presence can improve mental well-being and make public spaces more inviting. In dense cities, these benefits are especially valuable. Protecting and expanding urban tree cover is a practical form of climate adaptation.",
                        sectionOneTitle = "Benefits in Cities",
                        sectionOneBody = "Urban trees reduce heat islands. They improve air quality. They provide shade. They enhance aesthetics. They reduce noise. They support wildlife. They improve mental health. They encourage outdoor activity. They increase property values. Cities become more sustainable.",
                        sectionTwoTitle = "How to Support",
                        sectionTwoBody = "Participate in planting programs. Protect existing trees. Water during dry periods. Avoid damaging roots. Support green policies. Educate communities. Volunteer locally. Report illegal cutting. Promote urban greenery. Care for your surroundings.",
                        bottomImageAsset = "learning/trees-lesson-4.jpg",
                        bottomCaption = "Green cities are better cities."
                    ),
                    LearningLesson(
                        id = "trees_5",
                        categoryId = "trees",
                        title = "Trees and Climate Change",
                        shortDescription = "Trees are natural climate solutions.",
                        imageAsset = "learning/trees-lesson-5.jpg",
                        introText = "Trees play a major role in climate regulation. They absorb carbon from the atmosphere and store it in trunks, roots, and soils over long periods. Forest systems also influence rainfall, temperature, and resilience against climate stress. Because of this, trees are one of the most important natural climate tools available. Protecting them is as important as planting new ones.",
                        sectionOneTitle = "Climate Benefits",
                        sectionOneBody = "Trees absorb greenhouse gases. They store carbon long-term. They regulate temperatures. They influence rainfall. They reduce extreme weather impact. Forests act as carbon sinks. They slow climate change. They stabilize ecosystems. They support resilience. They are essential for mitigation.",
                        sectionTwoTitle = "Your Role",
                        sectionTwoBody = "Support reforestation projects. Reduce carbon footprint. Plant trees locally. Protect forests. Educate others. Advocate for policies. Choose sustainable products. Participate in initiatives. Stay informed. Take action consistently.",
                        bottomImageAsset = "learning/trees-lesson-5.jpg",
                        bottomCaption = "Trees fight climate change."
                    ),
                    LearningLesson(
                        id = "trees_6",
                        categoryId = "trees",
                        title = "Forest Biodiversity",
                        shortDescription = "Forests support diverse ecosystems.",
                        imageAsset = "learning/trees-lesson-6.jpg",
                        introText = "Biodiversity depends heavily on forest ecosystems. Forests provide food, shelter, and breeding conditions for huge numbers of species. When those systems are healthy, they support resilience and ecological balance. When they are damaged, many species decline together. Protecting biodiversity means protecting forests as living networks, not just collections of trees.",
                        sectionOneTitle = "Why It Matters",
                        sectionOneBody = "Forests host many species. They provide food and shelter. Biodiversity supports ecosystem stability. It improves resilience. It maintains balance. Species depend on forests. Loss leads to extinction. Healthy forests are diverse. Biodiversity supports life. It is essential for sustainability.",
                        sectionTwoTitle = "Protecting Biodiversity",
                        sectionTwoBody = "Protect natural habitats. Avoid harmful products. Support conservation. Reduce pollution. Promote sustainable practices. Educate others. Support protected areas. Avoid deforestation. Encourage restoration. Stay engaged.",
                        bottomImageAsset = "learning/trees-lesson-6.jpg",
                        bottomCaption = "Protect biodiversity."
                    ),
                    LearningLesson(
                        id = "trees_7",
                        categoryId = "trees",
                        title = "Agroforestry",
                        shortDescription = "Combine trees with agriculture.",
                        imageAsset = "learning/trees-lesson-7.jpg",
                        introText = "Agroforestry integrates trees into farming systems. It combines food production with ecological functions that improve soil, water retention, and resilience. Trees can support crops, animals, and incomes at the same time when placed thoughtfully. This makes farmland more diverse and stable over time. Agroforestry shows how agriculture and restoration can work together.",
                        sectionOneTitle = "Benefits",
                        sectionOneBody = "Improves soil fertility. Increases crop yields. Reduces erosion. Supports biodiversity. Enhances resilience. Provides additional income. Reduces chemical use. Improves water retention. Supports sustainability. Benefits farmers.",
                        sectionTwoTitle = "How It Works",
                        sectionTwoBody = "Plant trees alongside crops. Choose compatible species. Maintain balance. Monitor soil health. Use sustainable practices. Adapt to climate. Share knowledge. Support farmers. Expand gradually. Promote systems.",
                        bottomImageAsset = "learning/trees-lesson-7.jpg",
                        bottomCaption = "Trees support farming."
                    ),
                    LearningLesson(
                        id = "trees_8",
                        categoryId = "trees",
                        title = "Tree Care Basics",
                        shortDescription = "Healthy trees require proper care.",
                        imageAsset = "learning/trees-lesson-8.jpg",
                        introText = "Caring for trees ensures long-term benefits. Planting is only the beginning, because health and survival depend on proper maintenance. Watering, soil care, and protection from damage are all part of responsible stewardship. Healthy trees are stronger, safer, and more effective in the roles they play. Long-term care turns intention into lasting impact.",
                        sectionOneTitle = "Essential Care",
                        sectionOneBody = "Water regularly. Protect roots. Prune carefully. Monitor health. Prevent disease. Use proper soil. Provide nutrients. Avoid damage. Support growth. Maintain consistently.",
                        sectionTwoTitle = "Best Practices",
                        sectionTwoBody = "Learn tree species needs. Avoid overwatering. Protect from pests. Use mulch. Support young trees. Inspect regularly. Seek expert advice. Maintain environment. Care long-term. Be consistent.",
                        bottomImageAsset = "learning/trees-lesson-8.jpg",
                        bottomCaption = "Care for trees."
                    ),
                    LearningLesson(
                        id = "trees_9",
                        categoryId = "trees",
                        title = "Reforestation",
                        shortDescription = "Restore forests at scale.",
                        imageAsset = "learning/trees-lesson-9.jpg",
                        introText = "Reforestation rebuilds lost ecosystems. It helps restore tree cover in areas where forests were cleared or degraded. Done well, it supports biodiversity, carbon storage, soil recovery, and water systems together. Reforestation can also create local jobs and strengthen community resilience. It is one of the clearest ways to turn environmental damage into recovery.",
                        sectionOneTitle = "Why It Matters",
                        sectionOneBody = "Restores ecosystems. Absorbs carbon. Supports biodiversity. Improves soil. Enhances water cycles. Reduces climate impact. Supports communities. Rebuilds habitats. Promotes sustainability. Long-term benefits.",
                        sectionTwoTitle = "How to Support",
                        sectionTwoBody = "Donate to projects. Volunteer locally. Spread awareness. Support policies. Plant trees. Educate others. Reduce footprint. Choose sustainable products. Stay engaged. Contribute regularly.",
                        bottomImageAsset = "learning/trees-lesson-9.jpg",
                        bottomCaption = "Restore forests."
                    ),
                    LearningLesson(
                        id = "trees_10",
                        categoryId = "trees",
                        title = "Future of Forests",
                        shortDescription = "Forests need protection and innovation.",
                        imageAsset = "learning/trees-lesson-10.jpg",
                        introText = "The future of forests depends on our actions. Better monitoring, stronger policy, and smarter land management are making protection more effective. At the same time, consumption patterns and political choices still shape outcomes globally. Forests can recover and remain resilient if pressure is reduced in time. Their future is not fixed, which makes present decisions especially important.",
                        sectionOneTitle = "Emerging Solutions",
                        sectionOneBody = "Technology supports monitoring. Satellite data tracks forests. Policies improve protection. Sustainable forestry grows. Awareness increases. Innovation helps conservation. Communities participate. Restoration improves. Solutions are expanding. The future depends on action.",
                        sectionTwoTitle = "Your Impact",
                        sectionTwoBody = "Stay informed. Support conservation. Reduce consumption. Advocate change. Educate others. Protect nature. Make sustainable choices. Participate locally. Encourage others. Act consistently.",
                        bottomImageAsset = "learning/trees-lesson-10.jpg",
                        bottomCaption = "Protect the future."
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
