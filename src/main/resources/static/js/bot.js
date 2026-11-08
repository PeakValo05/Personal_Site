const chatForm = document.getElementById("chatComposer");
const sendButton = document.getElementById("aiSendBtn");
const messageInput = document.getElementById("aiMessage");
const chatMessages = document.getElementById("aiChatMessages");

const addMessageToChat = (message, sender) => {
    const messageElement = document.createElement("p");
    messageElement.className = `chat-message ${sender}-message`;
    messageElement.textContent = message;
    chatMessages.appendChild(messageElement);
    chatMessages.scrollTop = chatMessages.scrollHeight;
};

const setChatBusy = (isBusy) => {
    sendButton.disabled = isBusy;
    messageInput.disabled = isBusy;
    chatForm.setAttribute("aria-busy", String(isBusy));
};

const sendMessage = async (event) => {
    event.preventDefault();

    const message = messageInput.value.trim();
    if (!message || sendButton.disabled) {
        return;
    }

    addMessageToChat(message, "user");
    messageInput.value = "";
    setChatBusy(true);
    addMessageToChat("Thinking...", "bot");

    const pendingMessage = chatMessages.lastElementChild;
    pendingMessage.classList.add("is-pending");

    try {
        const response = await fetch("/api/ai/chat", {
            method: "POST",
            headers: {
                "Content-Type": "text/plain; charset=UTF-8",
                "Accept": "text/plain"
            },
            body: message
        });

        if (!response.ok) {
            throw new Error(`Chat request failed with status ${response.status}`);
        }

        const botReply = (await response.text()).trim();
        pendingMessage.remove();
        addMessageToChat(botReply || "I do not have a response yet. Please try again.", "bot");
    } catch (error) {
        pendingMessage.remove();
        addMessageToChat("I could not connect right now. Please try again or use the contact form.", "bot");
        console.error("AI chat request failed:", error);
    } finally {
        setChatBusy(false);
        messageInput.focus();
    }
};

chatForm.addEventListener("submit", sendMessage);