package com.example.data.local

import com.example.data.model.EventStatus
import com.example.data.model.MenuCourse
import com.example.data.model.ProposalStatus

object InitialData {

    suspend fun populateInitialData(dao: SocialDiningDao) {
        // 1. Restaurants
        val restaurants = listOf(
            RestaurantEntity(
                id = 1,
                name = "Osteria Del Fico",
                cuisine = "Tuscan & Coastal Italian",
                address = "482 Mulberry Lane, Soho",
                neighborhood = "Downtown Soho",
                rating = 4.9f,
                reviewCount = 184,
                coversGuaranteedTotal = 142,
                totalRevenueGenerated = 13490.0,
                description = "Warm exposed brick, historic wooden beams, and a wood-burning hearth. Specialized in handcrafted pasta and low-intervention natural wines.",
                minCoversForPrivateRoom = 8,
                maxRoomCapacity = 14,
                contactName = "Matteo Rossi (GM)",
                isPartnerVerified = true,
                offPeakSlotsJson = ModelMappers.stringListToJson(listOf("Tuesday Supper", "Thursday Tasting", "Sunday Twilight"))
            ),
            RestaurantEntity(
                id = 2,
                name = "Komorebi Charcoal & Raw",
                cuisine = "Modern Japanese Robata",
                address = "118 Mercer St",
                neighborhood = "West Village",
                rating = 4.8f,
                reviewCount = 215,
                coversGuaranteedTotal = 196,
                totalRevenueGenerated = 22540.0,
                description = "Intimate binchotan charcoal grill counter with rare sake pairings and fresh coastal sashimi selections.",
                minCoversForPrivateRoom = 6,
                maxRoomCapacity = 12,
                contactName = "Kenji Sato (Owner)",
                isPartnerVerified = true,
                offPeakSlotsJson = ModelMappers.stringListToJson(listOf("Wednesday Omakase", "Thursday Charcoal Club", "Monday Industry Night"))
            ),
            RestaurantEntity(
                id = 3,
                name = "Casa de Fuego",
                cuisine = "Contemporary Oaxacan",
                address = "79 Bowery Arts District",
                neighborhood = "Lower East Side",
                rating = 4.9f,
                reviewCount = 162,
                coversGuaranteedTotal = 88,
                totalRevenueGenerated = 9680.0,
                description = "Vibrant open-flame cooking featuring heirloom heirloom corn masa, 30-ingredient moles, and artisanal agave spirits.",
                minCoversForPrivateRoom = 8,
                maxRoomCapacity = 16,
                contactName = "Valeria Ramos (Beverage & Events Director)",
                isPartnerVerified = true,
                offPeakSlotsJson = ModelMappers.stringListToJson(listOf("Tuesday Mezcal Table", "Wednesday Mole Night"))
            ),
            RestaurantEntity(
                id = 4,
                name = "L'Atelier Botanique",
                cuisine = "French Conservatory & Farm-to-Table",
                address = "220 Greenpoint Ave",
                neighborhood = "Greenpoint Waterfront",
                rating = 4.7f,
                reviewCount = 98,
                coversGuaranteedTotal = 64,
                totalRevenueGenerated = 6720.0,
                description = "Glass conservatory filled with fragrant citrus trees and edible herbs. Seasonally driven French bistro fare.",
                minCoversForPrivateRoom = 10,
                maxRoomCapacity = 18,
                contactName = "Chloe Laurent",
                isPartnerVerified = true,
                offPeakSlotsJson = ModelMappers.stringListToJson(listOf("Thursday Salon", "Sunday Herbal Supper"))
            )
        )
        dao.insertRestaurants(restaurants)

        // 2. Hosts / Influencers
        val hosts = listOf(
            HostEntity(
                id = 1,
                name = "Elena Rostova",
                handle = "@elena.uncorked",
                niche = "Natural Wine & Mediterranean Gastronomy",
                followerCount = "128K",
                bio = "Sommelier & food storyteller exploring honest terroir, skin-contact wines, and communal feast culture.",
                rating = 4.95f,
                dinnersHostedCount = 18,
                totalEarnings = 4680.0,
                signatureDishStyle = "Coastal Crudo & Amphora Fermentations",
                commissionPercent = 20
            ),
            HostEntity(
                id = 2,
                name = "Marcus Vance",
                handle = "@marcusgrills",
                niche = "Culinary Culture & Streetwear Hospitality",
                followerCount = "240K",
                bio = "Creating elevated high-energy dining salons where chefs test unreleased menu concepts for curious diners.",
                rating = 4.88f,
                dinnersHostedCount = 24,
                totalEarnings = 7240.0,
                signatureDishStyle = "Charcoal Wagyu & Craft Highballs",
                commissionPercent = 20
            ),
            HostEntity(
                id = 3,
                name = "Chef Diego Morales",
                handle = "@diegomole",
                niche = "Ancestral Mexican & Agave Pairings",
                followerCount = "95K",
                bio = "Culinary researcher bridging pre-Hispanic recipes with modern Michelin techniques.",
                rating = 4.92f,
                dinnersHostedCount = 12,
                totalEarnings = 2850.0,
                signatureDishStyle = "Black Mole & Wild Forest Mushrooms",
                commissionPercent = 20
            ),
            HostEntity(
                id = 4,
                name = "Aria Chen",
                handle = "@ariaintable",
                niche = "Design, Architecture & Mindful Dining",
                followerCount = "82K",
                bio = "Host of sensory dining experiments combining tactile ceramics, floral aromatics, and deep table conversation.",
                rating = 4.85f,
                dinnersHostedCount = 9,
                totalEarnings = 1920.0,
                signatureDishStyle = "Botanical Consommé & Hand-Rolled Soba",
                commissionPercent = 20
            )
        )
        dao.insertHosts(hosts)

        // 3. Dinner Events
        val dinnerEvents = listOf(
            DinnerEventEntity(
                id = 1,
                title = "Sicilian Crudo & Amphora Wine Gathering",
                description = "An intimate communal feast curated around fresh day-boat crudo, handmade busiate pasta with trapani pesto, and rare orange amphora wines from Mount Etna. Elena leads intimate guided tastings between courses.",
                hostId = 1,
                hostName = "Elena Rostova",
                hostHandle = "@elena.uncorked",
                hostNiche = "Natural Wine & Mediterranean",
                hostFollowers = "128K",
                hostRating = 4.95f,
                venueId = 1,
                venueName = "Osteria Del Fico",
                venueAddress = "482 Mulberry Lane, Soho",
                venueNeighborhood = "Downtown Soho",
                dateString = "Thursday, Oct 24",
                timeString = "7:30 PM - 10:30 PM",
                pricePerSeat = 115.0,
                totalSeats = 12,
                bookedSeats = 10,
                minCoversRequired = 8,
                status = EventStatus.CONFIRMED.name,
                menuCoursesJson = ModelMappers.menuCoursesToJson(
                    listOf(
                        MenuCourse(1, "Hokkaido Scallop Crudo", "Blood orange reduction, Castelvetrano olive oil, sea fennel", "2022 Arianna Occhipinti SP68 Bianco"),
                        MenuCourse(2, "Busiate Trapanese", "Hand-twisted pasta with roasted Sicilian almonds, heirloom tomatoes, wild basil", "2021 COS Pithos Bianco Amphora"),
                        MenuCourse(3, "Slow-Roasted Amberjack", "Charred caponata, fennel pollen, crispy capers", "2020 Frank Cornelissen Susucaru"),
                        MenuCourse(4, "Bronte Pistachio Gelato & Olive Oil Cake", "Maldon smoked salt, warm fig syrup", "Averna Amaro Riserva")
                    )
                ),
                vibeTagsJson = ModelMappers.stringListToJson(listOf("Wine Pairings", "Seafood Lovers", "Conversational", "Table Confirmed")),
                dressCode = "Warm Earth Tones & Smart Casual",
                icebreakerPromptsJson = ModelMappers.stringListToJson(
                    listOf(
                        "What is a food memory that still makes you nostalgic?",
                        "If you had to pack one bottle of wine for a deserted island?",
                        "Best meal you've ever had under $10 vs over $100?"
                    )
                ),
                hostCutPercent = 20,
                specialNotes = "Includes all 4 courses and 4 full sommelier wine pairings."
            ),
            DinnerEventEntity(
                id = 2,
                title = "Smoked Mole Nocturne & Ancestral Mezcal",
                description = "Chef Diego curates a dramatic sensory supper in Casa de Fuego's candlelit private cellar. 3 distinct varieties of house-made moles aged up to 365 days, paired with micro-batch single-village mezcals.",
                hostId = 3,
                hostName = "Chef Diego Morales",
                hostHandle = "@diegomole",
                hostNiche = "Ancestral Mexican & Agave",
                hostFollowers = "95K",
                hostRating = 4.92f,
                venueId = 3,
                venueName = "Casa de Fuego",
                venueAddress = "79 Bowery Arts District",
                venueNeighborhood = "Lower East Side",
                dateString = "Friday, Nov 1",
                timeString = "8:00 PM - 11:00 PM",
                pricePerSeat = 125.0,
                totalSeats = 14,
                bookedSeats = 7,
                minCoversRequired = 9,
                status = EventStatus.PENDING_COVERS.name,
                menuCoursesJson = ModelMappers.menuCoursesToJson(
                    listOf(
                        MenuCourse(1, "Blue Corn Tetela", "Wild foraged mushrooms, hoja santa, queso de hebra", "El Jolgorio Tobalá Mezcal"),
                        MenuCourse(2, "Charred Octopus in Mole Amarillo", "Chayote, pickled mustard seeds, roasted bone marrow", "Vago Elote Mezcal"),
                        MenuCourse(3, "Braised Short Rib in 365-Day Mole Negro", "Plátano macho, hand-pressed heirloom tortillas", "Real Minero Pechuga"),
                        MenuCourse(4, "Smoked Oaxacan Chocolate Tart", "Ancho chile crisp, mezcal caramel", "Pasita artisanal digestif")
                    )
                ),
                vibeTagsJson = ModelMappers.stringListToJson(listOf("Open Flame", "Rare Spirits", "2 Seats to Lock Table", "Food Historian")),
                dressCode = "Moody Cocktail Attire",
                icebreakerPromptsJson = ModelMappers.stringListToJson(
                    listOf(
                        "What is the most adventurous ingredient you've ever tried?",
                        "What dinner conversation changed your perspective on something important?"
                    )
                ),
                hostCutPercent = 22,
                specialNotes = "2 more bookings needed to trigger restaurant private room lock-in."
            ),
            DinnerEventEntity(
                id = 3,
                title = "Midnight Charcoal Robata & Tokyo Highballs",
                description = "High energy, smoky aromas, and upbeat curated vinyl music. Marcus hosts foodies and creators around Komorebi's heated private courtyard for unreleased skewers and Japanese whisky cocktails.",
                hostId = 2,
                hostName = "Marcus Vance",
                hostHandle = "@marcusgrills",
                hostNiche = "Culinary Culture & Streetwear",
                hostFollowers = "240K",
                hostRating = 4.88f,
                venueId = 2,
                venueName = "Komorebi Charcoal & Raw",
                venueAddress = "118 Mercer St",
                venueNeighborhood = "West Village",
                dateString = "Saturday, Nov 9",
                timeString = "9:00 PM - Midnight",
                pricePerSeat = 140.0,
                totalSeats = 10,
                bookedSeats = 10,
                minCoversRequired = 8,
                status = EventStatus.CONFIRMED.name,
                menuCoursesJson = ModelMappers.menuCoursesToJson(
                    listOf(
                        MenuCourse(1, "A5 Miyazaki Wagyu Skewers", "Tare glaze, freshly grated wasabi root", "Suntory Toki Sonic Highball"),
                        MenuCourse(2, "King Oyster Mushroom Yakitori", "Egg yolk confit, black winter truffle butter", "Kikusui Junmai Ginjo"),
                        MenuCourse(3, "Smoked Miso Black Cod", "Charred scallion, yuzu dashi reduction", "Mars Iwai 45 Mizuwari"),
                        MenuCourse(4, "Matcha Basque Burnt Cheesecake", "Sesame brittle, kuromitsu syrup", "Hojicha smoked tea")
                    )
                ),
                vibeTagsJson = ModelMappers.stringListToJson(listOf("Sold Out", "Vinyl Beats", "Creative Salon", "Late Night")),
                dressCode = "Streetwear & Elevated Casual",
                icebreakerPromptsJson = ModelMappers.stringListToJson(
                    listOf(
                        "What is your dream creative project you haven't started yet?",
                        "The most overrated dining trend right now?"
                    )
                ),
                hostCutPercent = 25,
                specialNotes = "Table fully confirmed! Waiting list open."
            ),
            DinnerEventEntity(
                id = 4,
                title = "Botanical Greenhouse Salon & French Harvest",
                description = "An afternoon sunlit garden gathering surrounded by flowering jasmine and lemon trees. Aria pairs organic seasonal flora, hand-pressed herbal oils, and delicate French pastry.",
                hostId = 4,
                hostName = "Aria Chen",
                hostHandle = "@ariaintable",
                hostNiche = "Design & Mindful Dining",
                hostFollowers = "82K",
                hostRating = 4.85f,
                venueId = 4,
                venueName = "L'Atelier Botanique",
                venueAddress = "220 Greenpoint Ave",
                venueNeighborhood = "Greenpoint Waterfront",
                dateString = "Sunday, Nov 17",
                timeString = "1:00 PM - 4:00 PM",
                pricePerSeat = 95.0,
                totalSeats = 16,
                bookedSeats = 5,
                minCoversRequired = 10,
                status = EventStatus.PENDING_COVERS.name,
                menuCoursesJson = ModelMappers.menuCoursesToJson(
                    listOf(
                        MenuCourse(1, "Heirloom Tomato & Verbena Tart", "Whipped goat curd, borage blossoms", "Domaine Tempier Rosé"),
                        MenuCourse(2, "Wild Chanterelle Velouté", "Tarragon brioche croutons, truffle oil", "Sancerre Blanc 2021"),
                        MenuCourse(3, "Pan-Roasted Duck Breast with Lavender Honey", "Glazed rainbow carrots, parsnip purée", "Burgundy Pinot Noir"),
                        MenuCourse(4, "Elderflower & Meyer Lemon Mille-Feuille", "Chamomile spun sugar", "Sparkling Herbal Tisane")
                    )
                ),
                vibeTagsJson = ModelMappers.stringListToJson(listOf("Sunlit Greenhouse", "Design Community", "Farm-to-Table")),
                dressCode = "Garden Chic / Linens & Pastels",
                icebreakerPromptsJson = ModelMappers.stringListToJson(
                    listOf(
                        "If you designed your ultimate dining room, what would the center piece be?",
                        "What's one daily ritual you never skip?"
                    )
                ),
                hostCutPercent = 20,
                specialNotes = "5 spots booked. Needs 5 more guests to guarantee the private greenhouse."
            )
        )
        dao.insertDinnerEvents(dinnerEvents)

        // 4. Initial Bookings (for Diner view & host attendee lists)
        val bookings = listOf(
            BookingEntity(
                id = 1,
                dinnerEventId = 1,
                dinnerTitle = "Sicilian Crudo & Amphora Wine Gathering",
                venueName = "Osteria Del Fico",
                dateString = "Thursday, Oct 24",
                timeString = "7:30 PM - 10:30 PM",
                guestName = "Jordan Hayes",
                guestEmail = "jordan.hayes@example.com",
                seatsCount = 2,
                pricePerSeat = 115.0,
                totalPrice = 230.0,
                dietaryRestrictions = "Pescatarian, no dairy",
                seatingVibe = "Conversationalist",
                qrTicketCode = "TAVOLA-PASS-8842-CONFIRMED",
                bookingTimestamp = System.currentTimeMillis() - 86400000L * 2,
                isCheckedIn = false
            ),
            BookingEntity(
                id = 2,
                dinnerEventId = 1,
                dinnerTitle = "Sicilian Crudo & Amphora Wine Gathering",
                venueName = "Osteria Del Fico",
                dateString = "Thursday, Oct 24",
                timeString = "7:30 PM - 10:30 PM",
                guestName = "Sophia Lin",
                guestEmail = "sophia@creatorstudio.co",
                seatsCount = 1,
                pricePerSeat = 115.0,
                totalPrice = 115.0,
                dietaryRestrictions = "None",
                seatingVibe = "Foodie Corner",
                qrTicketCode = "TAVOLA-PASS-9124-CONFIRMED",
                bookingTimestamp = System.currentTimeMillis() - 86400000L,
                isCheckedIn = true
            )
        )
        bookings.forEach { dao.insertBooking(it) }

        // 5. Initial Collaboration Proposals (Host <-> Restaurant partnership pitches)
        val proposals = listOf(
            CollaborationProposalEntity(
                id = 1,
                hostId = 1,
                hostName = "Elena Rostova",
                hostHandle = "@elena.uncorked",
                hostFollowers = "128K",
                hostNiche = "Natural Wine",
                restaurantId = 2,
                restaurantName = "Komorebi Charcoal & Raw",
                eventTitle = "Raw Bar & Low-Intervention Sake Night",
                proposedDate = "Tuesday, Nov 12",
                proposedTime = "7:00 PM - 10:00 PM",
                targetCovers = 12,
                minGuaranteeCovers = 8,
                pricePerSeat = 130.0,
                pitchNote = "Tuesday nights are traditionally slower for West Village robata counters. My audience loves unfiltered sparkling sake and crudo pairings. I guarantee 8 covers minimum!",
                menuVision = "4-course raw seafood tasting with unpasteurized nama sake flights.",
                status = ProposalStatus.PENDING.name,
                timestamp = System.currentTimeMillis() - 3600000L * 5
            ),
            CollaborationProposalEntity(
                id = 2,
                hostId = 2,
                hostName = "Marcus Vance",
                hostHandle = "@marcusgrills",
                hostFollowers = "240K",
                hostNiche = "Culinary Culture & Streetwear",
                restaurantId = 3,
                restaurantName = "Casa de Fuego",
                eventTitle = "Wood-Fired Wagyu & Mezcal Tasting Salon",
                proposedDate = "Thursday, Nov 21",
                proposedTime = "8:30 PM - 11:30 PM",
                targetCovers = 16,
                minGuaranteeCovers = 10,
                pricePerSeat = 150.0,
                pitchNote = "We will bring 16 top tier creators and food enthusiasts on Thursday evening. Venue guarantees $2,400 minimum bar/food spend.",
                menuVision = "Smoked wagyu picanha tacos with clay pot beans and rare mezcal flight.",
                status = ProposalStatus.ACCEPTED.name,
                timestamp = System.currentTimeMillis() - 86400000L * 3
            )
        )
        proposals.forEach { dao.insertProposal(it) }
    }
}
