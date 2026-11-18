# Task 4 – Forensics
The provided code spells the callback `mouseclicked`; the correct name is `mouseClicked` (case-sensitive). Thus the adapter method is not overridden and the click count never changes. Drawing with `getGraphics()` is temporary; minimizing/restoring triggers repaint and loses the marks. The repair stores click Points, updates the label, calls repaint(), and redraws every point in paint(). `@Override` makes the compiler reject a misspelled callback.

Alternative BufferedImage: point-list repaint costs O(n) for n points and stores O(n) points. An off-screen ARGB image uses O(width×height) pixels (about 4 bytes/pixel) and repaint can copy the image rather than redraw each point. Point list is compact for sparse marks; image can be faster for repeated dense repainting.

Evidence to capture: dots/count; minimize and restore with dots retained; temporarily misspell callback with @Override and capture compiler error, then restore.
