    function scrollPhotos(direction) {
        const track = document.getElementById("photoTrack");
        const card = track.querySelector(".photo-card");

        if (!card) return;

        const gap = parseFloat(getComputedStyle(track).gap) || 0;

        track.scrollBy({
            left: direction * (card.offsetWidth + gap),
            behavior: "smooth"
        });
    }