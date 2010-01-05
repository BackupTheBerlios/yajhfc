Name:         yajhfc-faxprinter
Summary:      Install a virtual fax printer for YajHFC
Version:      0.4.2.99beta1
Release:      1
License:      GNU General Public License (GPL)
Group:        Hardware/Fax
Source:       yajhfc-%{version}-rpmsrc.tgz
URL:          http://yajhfc.berlios.de/
Vendor:       Jonas Wolz
BuildRoot:    %{_tmppath}/%{name}-%{version}-build
Requires:     yajhfc, cups, cups-client
BuildArch:    noarch

%description
This package installs a virtual fax printer in CUPS for YajHFC.

When a user prints to this printer, the output is redirected to YajHFC and
a "send fax" dialog is shown.

YajHFC must be running when a user wishes to print to the virtual fax printer.
Because of that, this package automatically adds an autostart link for all 
desktop environments conforming to the Free Desktop Menu Specification (e.g. KDE, GNOME, XFCE).

%prep
%setup -n yajhfc-0.4.3beta1
%build
# Do nothing
%install
sh install-faxprinter.sh "%{buildroot}"

%clean
%__rm -rf "%{buildroot}"

%post
        if lpstat -p yajhfc >/dev/null 2>&1 ; then
	  echo 'YajHFC fax printer already exists.'
	else
       	  lpadmin -p yajhfc -D 'YajHFC virtual fax printer' -L 'localhost' -v 'yajhfc:/tmp/yajhfc-$USERNAME' -E -P /usr/share/yajhfc/yajhfc.ppd -o "printer-error-policy=abort-job" -o "printer-is-shared=false"
	  echo 'Virtual fax printer "yajhfc" created. (Re)Start YajHFC to fully enable it.'
	fi

%preun
	lpadmin -x yajhfc || true
%files
%defattr(-, root, root)
%doc doc/COPYING
/etc/yajhfc/settings.override
/etc/xdg/autostart/yajhfc-autostart.desktop
/usr/share/yajhfc/yajhfc.ppd
/usr/lib/cups/backend/yajhfc

%changelog
* Tue Jan 05 2010 - jonas.wolz@freenet.de
- initial RPM for YajHFC on openSUSE 
