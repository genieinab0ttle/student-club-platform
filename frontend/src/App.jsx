import { useState, useEffect } from "react";
import "./App.css";

const API = "http://localhost:8080/api";

function App() {
  const [meeting, setMeeting] = useState({
    title: "",
    date: "",
    time: "",
    location: "",
  });

  const [meetings, setMeetings] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [notificationError, setNotificationError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  async function loadNotifications() {
    try {
      const response = await fetch(`${API}/notifications`);

      if (!response.ok) {
        throw new Error("Failed to load notifications.");
      }

      const data = await response.json();

      setNotifications(
        [...data].sort(
          (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
        )
      );
      setNotificationError("");
    } catch (error) {
      setNotificationError("Could not load notifications.");
      console.error(error);
    }
  }

  useEffect(() => {
    fetch(`${API}/meetings`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to load meetings.");
        }
        return response.json();
      })
      .then((data) => setMeetings(data))
      .catch((error) => console.error(error));

    loadNotifications();

    const timer = setInterval(loadNotifications, 5000);
    return () => clearInterval(timer);
  }, []);

  function handleChange(event) {
    setMeeting((current) => ({
      ...current,
      [event.target.name]: event.target.value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    if (submitting) return;
    setSubmitting(true);

    try {
      const response = await fetch(`${API}/meetings`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(meeting),
      });

      if (!response.ok) {
        const message = await response.text();
        throw new Error(message || "Failed to create meeting.");
      }

      const savedMeeting = await response.json();

      setMeetings((current) => [...current, savedMeeting]);
      setMeeting({
        title: "",
        date: "",
        time: "",
        location: "",
      });

      await loadNotifications();

      alert(`Meeting "${savedMeeting.title}" created successfully!`);
    } catch (error) {
      alert(error.message);
      console.error(error);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <main className="container">
      <h1>Student Club Management</h1>
      <h2>Create a Club Meeting</h2>

      <form onSubmit={handleSubmit}>
        <label htmlFor="title">Meeting title</label>
        <input
          id="title"
          name="title"
          value={meeting.title}
          onChange={handleChange}
          required
        />

        <label htmlFor="date">Date</label>
        <input
          id="date"
          type="date"
          name="date"
          value={meeting.date}
          onChange={handleChange}
          required
        />

        <label htmlFor="time">Time</label>
        <input
          id="time"
          type="time"
          name="time"
          value={meeting.time}
          onChange={handleChange}
          required
        />

        <label htmlFor="location">Location</label>
        <input
          id="location"
          name="location"
          value={meeting.location}
          onChange={handleChange}
          required
        />

        <button type="submit" disabled={submitting}>
          {submitting ? "Creating..." : "Create Meeting"}
        </button>
      </form>

      <section>
        <h2>Notifications</h2>

        {notificationError ? (
          <p role="alert">{notificationError}</p>
        ) : notifications.length === 0 ? (
          <p>No notifications yet.</p>
        ) : (
          notifications.map((item) => (
            <div key={item.eventId}>
              <p>
                <strong>{item.message}</strong>
              </p>
              <small>
                {new Date(item.createdAt).toLocaleString("en-GB")}
              </small>
            </div>
          ))
        )}
      </section>

      <section>
        <h2>All Club Meetings</h2>

        {meetings.length === 0 ? (
          <p>No meetings yet.</p>
        ) : (
          meetings.map((item) => (
            <div key={item.id}>
              <h3>{item.title}</h3>
              <p>
                {item.date} at {item.time}
              </p>
              <p>{item.location}</p>
            </div>
          ))
        )}
      </section>
    </main>
  );
}

export default App;