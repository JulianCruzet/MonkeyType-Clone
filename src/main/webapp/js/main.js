let ws;
let currentRoomId = null;

// this function closes the websocket
function leaveCurrentRoom() {
    if (ws && ws.readyState !== WebSocket.CLOSED) {
        console.log("Closing WebSocket connection...");
        ws.close();
    }
}

// this function creates a new room
function newRoom(){

    leaveCurrentRoom(); // leave the current room before making a new one

    // calling the ChatServlet to retrieve a new room ID
    let callURL= "http://localhost:8080/speedtyper-1.0-SNAPSHOT/speedtyper-servlet";
    fetch(callURL, {
        method: 'GET',
        headers: {
            'Accept': 'text/plain',
        },
    })
        .then(response => response.text())
        .then(response => {
            // debugging
            console.log(response);

            currentRoomID = response;
            getRooms(); // populate room container with the new room
            leaveCurrentRoom();
        });
}

// this function gets and displays the list of rooms
function getRooms(){

    // calling the ChatServlet to display the existing room IDs
    let callURL = "http://localhost:8080/speedtyper-1.0-SNAPSHOT/room-list"
    fetch(callURL, {
        method: 'GET',
        headers: {
            'Accept' : 'application/json',
        },
    })
        .then(response => response.json())
        .then(data => {
            // debugging
            console.log(data);

            // add the room to the div
            const roomsContainer = document.getElementById('rooms');

            // clear existing rooms
            roomsContainer.innerHTML = '';

            for(let room in data) {
                // create a new room card
                const roomCard = document.createElement('div');
                roomCard.className = 'room-card';
                roomCard.title = 'click to join the room'
                roomCard.textContent = room;

                // add click event to the room card
                roomCard.addEventListener('click', () => {
                    // enter the room/initialize the websocket handshake
                })

                // append the room card to the div
                roomsContainer.appendChild(roomCard);
            }
        })
        .catch(error => {
            console.error('Error fetching the room list', error);
        })
}

// waits for the create room button to fetch a new sentence
document.addEventListener('DOMContentLoaded', function() {
    const startButton = document.getElementById('start-game');
    startButton.onclick = function() {
        console.log("button clicked")
        fetch('http://localhost:8080/speedtyper-1.0-SNAPSHOT/sentence-getter')
            .then(response => response.json())
            .then(data => {
                // debugging
                // console.log(data.sentence)

                newRoom();
                displaySentence(data.sentence);
            });
    };
});

// displays the sentence onto the screen, should replace the <p> in html line 57
function displaySentence(sentence) {
    const sentenceDisplay = document.getElementById('sentence');
    sentenceDisplay.textContent = sentence;
}

document.addEventListener('DOMContentLoaded', function() {
    const startButton = document.getElementById('start-game');
    const sendButton = document.getElementById('send-button');
    let startTime;
    let timerInterval;

    startButton.addEventListener('click', function() {
        startTime = new Date(); // Capture the start time when the game starts
        console.log("Timer started");
    });
    document.getElementById('user-input-container').addEventListener('submit', function(event) {
        event.preventDefault();  // Prevents the form from submitting normally

        const endTime = new Date(); // Capture the end time when the user submits their sentence
        elapsedTime = (endTime - startTime) / 1000; // Calculate elapsed time in seconds
        const timeInMinutes = elapsedTime / 60; // Convert time to minutes

        const typedSentence = document.getElementById('input').value;
        const words = typedSentence.split(/\s+/).length; // Calculate number of words typed
        const wpm = (words / timeInMinutes).toFixed(2); // Calculate words per minute

        document.getElementById('input').value = '';  // Clear the input field

        const originalSentence = document.getElementById('sentence').textContent; // Get the displayed sentence
        const resultsElement = document.getElementById('results');
        const timerElement = document.getElementById('timer-display');
        const wpmElement = document.getElementById('wpm-display'); // Make sure you have this element in your HTML

        // Send the typed and original sentences for accuracy calculation
        fetch('http://localhost:8080/speedtyper-1.0-SNAPSHOT/calculate-accuracy', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({typedSentence: typedSentence, originalSentence: originalSentence}),
        })
            .then(response => response.json())
            .then(data => {
                if (resultsElement) {
                    resultsElement.textContent = `Accuracy: ${data.accuracy}%`; // Display accuracy
                    timerElement.textContent = `Time: ${elapsedTime.toFixed(2)}s`; // Display the time taken
                    wpmElement.textContent = `WPM: ${wpm}`; // Display WPM
                } else {
                    console.error('Results element not found in the DOM');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });

})

    function updateTimer(startTime) {
    const currentTime = new Date();
    const elapsedTime = (currentTime - startTime) / 1000;
    document.getElementById('timer').textContent = `Timer: ${elapsedTime.toFixed(2)}s`;
}

